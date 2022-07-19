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

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.playerNotFound
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.func.usage
import dev.jaims.moducore.bukkit.perm.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DelhomeCommand(override val plugin: ModuCore) : BaseCommand {
    override val module: Property<Boolean> = Modules.COMMAND_HOMES

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            1 -> {
                if (!Permissions.DELHOME.has(sender)) return
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }
                val homeName = args.getOrNull(0) ?: run {
                    sender.usage(usage, description)
                    return
                }
                val homes = storageManager.loadPlayerData(sender.uniqueId).join().homes
                val success = homes.remove(homeName) != null
                if (!success) {
                    sender.send(Lang.HOME_NOT_FOUND, sender) { it.replace("{name}", homeName) }
                    return
                }
                sender.send(Lang.DELHOME_SUCCESS, sender) { it.replace("{name}", homeName) }
            }
            2 -> {
                if (!Permissions.DELHOME_OTHERS.has(sender)) return
                val homeName = args.getOrNull(0) ?: run {
                    sender.usage(usage, description)
                    return
                }
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return
                }
                val homes = storageManager.loadPlayerData(target.uniqueId).join().homes
                val success = homes.remove(homeName) != null
                if (!success) {
                    sender.send(Lang.HOME_NOT_FOUND, target) { it.replace("{name}", homeName) }
                    return
                }
                if (!props.isSilent) {
                    sender.send(Lang.DELHOME_SUCCESS, target) { it.replace("{name}", homeName) }
                }
                sender.send(Lang.DELHOME_SUCCESS_TARGET, target) { it.replace("{name}", homeName) }
            }
            else -> sender.usage(usage, description)
        }
    }

    override val usage: String = "/delhome <name> [target]"
    override val description: String = "Delete a home."
    override val commandName: String = "delhome"
}