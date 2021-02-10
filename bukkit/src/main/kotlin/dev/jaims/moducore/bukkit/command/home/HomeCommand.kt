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

import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import dev.jaims.moducore.api.event.ModuCoreTeleportHomeEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Perm
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.playerNotFound
import dev.jaims.moducore.bukkit.util.send
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand(override val plugin: ModuCore) : BaseCommand {

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        val cooldown = fileManager.config[Config.HOME_COOLDOWN]
        when (args.size) {
            0, 1 -> {
                if (!Perm.HOME.has(sender)) return
                val homes = storageManager.getPlayerData(sender.uniqueId).homes
                val name = args.getOrNull(0) ?: fileManager.config[Config.HOME_DEFAULT_NAME]
                val home = homes[name] ?: run {
                    sender.send(Lang.HOME_NOT_FOUND, sender) { it.replace("{name}", name) }
                    return
                }

                if (props.bypassCooldown || cooldown == 0) {
                    PaperLib.teleportAsync(sender, home.location)
                    sender.send(Lang.HOME_SUCCESS, sender) { it.replace("{name}", name) }
                    Bukkit.getServer().pluginManager.callEvent(ModuCoreTeleportHomeEvent(sender, name, home.location))
                    return
                }

                sender.send(Lang.HOME_SUCCESS, sender) { it.replace("{name}", name).replace("{cooldown}", cooldown.toString()) }

                val task = Bukkit.getScheduler().schedule(plugin, SynchronizationContext.ASYNC) {
                    // we wait the cooldown then switch to sync
                    waitFor((20 * cooldown).toLong())
                    // without the context switch, the teleportation below wont work.
                    switchContext(SynchronizationContext.SYNC)
                    sender.send(Lang.HOME_SUCCESS, sender) { it.replace("{name}", name) }
                    PaperLib.teleportAsync(sender, home.location)
                    Bukkit.getServer().pluginManager.callEvent(ModuCoreTeleportHomeEvent(sender, name, home.location))
                }

                cancelOnMove(sender, cooldown, task)
            }
            2 -> {
                if (!Perm.HOME_OTHERS.has(sender)) return
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return
                }
                val homes = storageManager.getPlayerData(target.uniqueId).homes
                val name = args.getOrNull(0) ?: fileManager.config[Config.HOME_DEFAULT_NAME]
                val home = homes[name] ?: run {
                    sender.send(Lang.HOME_NOT_FOUND, target) { it.replace("{name}", name) }
                    return
                }

                PaperLib.teleportAsync(sender, home.location)
                sender.send(Lang.HOME_SUCCESS_TARGET, target) { it.replace("{name}", name) }
                Bukkit.getServer().pluginManager.callEvent(ModuCoreTeleportHomeEvent(target, name, home.location))
            }
        }
    }

    override val usage: String = "/home [name] [target]"
    override val description: String = "Teleport to your default home or one with a specific name. If teleporting to a target's home" +
            "you must provide the name."
    override val commandName: String = "home"
}