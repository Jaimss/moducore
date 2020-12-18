/*
 * This file is a part of JCore, licensed under the MIT License.
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

package dev.jaims.jcore.bukkit.command

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.manager.config.Config
import dev.jaims.jcore.bukkit.manager.config.FileManager
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.jcore.bukkit.manager.noConsoleCommand
import dev.jaims.jcore.bukkit.manager.playerNotFound
import dev.jaims.jcore.bukkit.manager.usage
import dev.jaims.mcutils.bukkit.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand(private val plugin: JCore) : JCoreCommand {

    override val commandName = "fly"
    override val usage = "/fly [target]"
    override val description = "Enable fly for yourself or another player."

    private val playerManager = plugin.managers.playerManager
    private val fileManager = plugin.managers.fileManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // invalid args length
        if (args.size > 1) {
            sender.usage(usage, description)
            return false
        }

        when (args.size) {
            // for a single player
            0 -> {
                if (!Perm.FLY.has(sender)) return false
                // only fly for Players
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return false
                }
                sender.toggleFlight(fileManager)
            }
            // for a target player
            1 -> {
                if (!Perm.FLY_OTHERS.has(sender)) return false
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound()
                    return false
                }
                target.toggleFlight(fileManager, sender)
            }
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val completions = mutableListOf<String>()

        when (args.size) {
            1 -> completions.addAll(playerManager.getPlayerCompletions(args[0]))
        }

        return completions
    }

}

/**
 * Enable flight for a [this] and optionally [sendMessage] to the player letting them know they
 * now have flight enabled.
 * If [executor] is null, the player changed their own flight. If [executor] is not null, someone else changed
 * their flight.
 *
 * @return True if they are now flying, false if they were already flying.
 */
internal fun Player.enableFlight(
    fileManager: FileManager,
    executor: CommandSender? = null,
    sendMessage: Boolean = true,
) {
    // set them to flying
    allowFlight = true
    if (sendMessage) {
        when (executor) {
            null -> {
                send(fileManager.getString(Lang.FLIGHT_ENABLED, this))
            }
            else -> {
                if (fileManager.config.getProperty(Config.ALERT_TARGET)) {
                    send(fileManager.getString(Lang.FLIGHT_ENABLED, this))
                }
                executor.send(fileManager.getString(Lang.TARGET_FLIGHT_ENABLED, this))
            }
        }
    }
}

/**
 * Disable flight for a [this] and optionally [sendMessage] to the player letting them know they are no longer
 * flying.
 * If [executor] is null, the player changed their own flight. If [executor] is not null, someone else changed
 * their flight.
 *
 * @return True if they are no longer flying, false if they were already flying.
 */
internal fun Player.disableFlight(
    fileManager: FileManager,
    executor: CommandSender? = null,
    sendMessage: Boolean = true
) {
    allowFlight = false
    if (sendMessage) {
        when (executor) {
            null -> {
                send(fileManager.getString(Lang.FLIGHT_DISABLED, this))
            }
            else -> {
                if (fileManager.config.getProperty(Config.ALERT_TARGET)) {
                    send(fileManager.getString(Lang.FLIGHT_DISABLED, this))
                }
                executor.send(fileManager.getString(Lang.TARGET_FLIGHT_DISABLED, this))
            }
        }
    }
}

/**
 * Toggle flight using [disableFlight] and [enableFlight]
 */
internal fun Player.toggleFlight(
    fileManager: FileManager,
    executor: CommandSender? = null,
    sendMessage: Boolean = true
) {
    if (allowFlight) disableFlight(fileManager, executor, sendMessage)
    else enableFlight(fileManager, executor, sendMessage)
}
