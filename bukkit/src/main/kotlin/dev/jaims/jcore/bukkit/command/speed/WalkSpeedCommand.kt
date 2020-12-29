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

package dev.jaims.jcore.bukkit.command.speed

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.command.BaseCommand
import dev.jaims.jcore.bukkit.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WalkSpeedCommand(private val plugin: JCore) : BaseCommand {

    override val usage: String = "/walkspeed <amount> [target]"
    override val description: String = "Change your walk speed."
    override val commandName: String = "walkspeed"

    val playerManager = plugin.api.playerManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            1 -> {
                if (!Perm.WALKSPEED.has(sender)) return true
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return true
                }
                val speed = args[0].toIntOrNull() ?: run {
                    sender.invalidNumber()
                    return true
                }
                playerManager.setWalkSpeed(sender, speed)
            }
            2 -> {
                if (!Perm.WALKSPEED_OTHERS.has(sender)) return true
                val speed = args[0].toIntOrNull() ?: run {
                    sender.invalidNumber()
                    return true
                }
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return true
                }
                playerManager.setWalkSpeed(target, speed, sender)
            }
            else -> sender.usage(usage, description)
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val completions = mutableListOf<String>()

        when (args.size) {
            1 -> (0..10).forEach {
                if (it.toString().contains(args[0], ignoreCase = true)) completions.add(it.toString())
            }
            2 -> completions.addAll(playerManager.getPlayerCompletions(args[1]))
        }

        return completions
    }


}