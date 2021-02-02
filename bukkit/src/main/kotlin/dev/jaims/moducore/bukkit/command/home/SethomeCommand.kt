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

import dev.jaims.mcutils.bukkit.event.waitForEvent
import dev.jaims.moducore.api.data.LocationHolder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Perm
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent

class SethomeCommand(override val plugin: ModuCore) : BaseCommand {

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (Perm.SET_HOME_AMOUNT.getAmount(sender, true) == null) return
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
        // TODO ("Home amount check")
        data.homes[name] = LocationHolder.from(sender.location)
        fileManager.getMessage(Lang.HOME_SET_SUCCESS,
            sender,
            replacements = mapOf("{name}" to name, "{time}" to fileManager.config[Config.HOME_UNDO_TIMEOUT])).sendMessage(sender)

        // start a task to undo the sethome if they want
        plugin.waitForEvent<AsyncPlayerChatEvent>(
            timeoutTicks = 20 * fileManager.config[Config.HOME_UNDO_TIMEOUT],
            predicate = { it.player.uniqueId == sender.uniqueId && it.message == "undo" }
        ) {
            it.isCancelled = true
            if (oldHome != null) {
                data.homes[name] = oldHome
            } else {
                data.homes.remove(name)
            }
            fileManager.getMessage(Lang.HOME_SET_UNDONE, sender).sendMessage(sender)
        }
    }

    override val usage: String = "/sethome <name>"
    override val description: String = "Set a home at your current location. Will replace homes with the same name."
    override val commandName: String = "sethome"
}