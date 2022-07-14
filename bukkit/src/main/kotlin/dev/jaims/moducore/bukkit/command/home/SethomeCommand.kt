/*
 * This file is a part of ModuCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaims.moducore.bukkit.command.home

import dev.jaims.moducore.api.data.LocationHolder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.SpigotOnlyException
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.func.waitForEvent
import dev.jaims.moducore.common.message.plainText
import io.papermc.paper.event.player.AsyncChatEvent
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent

class SethomeCommand(override val plugin: ModuCore) : BaseCommand {
    override val module: Property<Boolean> = Modules.COMMAND_HOMES

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        val amount = Permissions.SET_HOME_AMOUNT.getAmount(sender, true) ?: return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        // get the name and data
        val name = args.getOrNull(0) ?: fileManager.config[Config.HOME_DEFAULT_NAME]
        val data = storageManager.getPlayerData(sender.uniqueId)
        // get the old home at that location for undo
        val oldHome = data.homes[name]
        // add the new home to the data
        if (data.homes.size >= amount && oldHome == null) {
            sender.send(Lang.HOME_SET_FAILURE, sender)
            return
        }
        data.homes[name] = LocationHolder.from(sender.location)
        sender.send(Lang.HOME_SET_SUCCESS, sender) {
            it.replace("{name}", name).replace("{time}", fileManager.config[Config.HOME_UNDO_TIMEOUT].toString())
        }

        // start a task to undo the sethome if they want
        try {
            // try the paper version
            plugin.waitForEvent<AsyncChatEvent>(
                timeoutTicks = 20 * fileManager.config[Config.HOME_UNDO_TIMEOUT],
                predicate = { it.player.uniqueId == sender.uniqueId && it.message().plainText() == "undo" },
                priority = EventPriority.LOWEST
            ) {
                it.isCancelled = true
                if (oldHome != null) {
                    data.homes[name] = oldHome
                } else {
                    data.homes.remove(name)
                }
                sender.send(Lang.HOME_SET_UNDONE, sender)
            }
        } catch (ignored: SpigotOnlyException) {
            // spigot version
            plugin.waitForEvent<AsyncPlayerChatEvent>(
                timeoutTicks = 20 * fileManager.config[Config.HOME_UNDO_TIMEOUT],
                predicate = { it.player.uniqueId == sender.uniqueId && it.message == "undo" },
                priority = EventPriority.LOWEST
            ) {
                it.isCancelled = true
                if (oldHome != null) {
                    data.homes[name] = oldHome
                } else {
                    data.homes.remove(name)
                }
                sender.send(Lang.HOME_SET_UNDONE, sender)
            }
        }
    }

    override val usage: String = "/sethome [name]"
    override val description: String = "Set a home at your current location. Will replace homes with the same name."
    override val commandName: String = "sethome"
}