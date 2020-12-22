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

package dev.jaims.jcore.bukkit.command.repair

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.command.JCoreCommand
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.util.noConsoleCommand
import dev.jaims.jcore.bukkit.util.playerNotFound
import dev.jaims.jcore.bukkit.util.usage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RepairAll(private val plugin: JCore) : JCoreCommand {

    override val usage: String = "/repairall [target]"
    override val description: String = "Repair all items for yourself or a target player."
    override val commandName: String = "repairall"

    private val playerManager = plugin.api.playerManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            0 -> {
                if (!Perm.REPAIR_ALL.has(sender)) return true
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return true
                }
                playerManager.repairAll(sender, null, true)
            }
            1 -> {
                if (!Perm.REPAIR_ALL_OTHERS.has(sender)) return true
                val target = playerManager.getTargetPlayer(args[0]) ?: kotlin.run {
                    sender.playerNotFound(args[0])
                    return true
                }
                playerManager.repairAll(target, sender, true)
            }
            else -> sender.usage(usage, description)
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val matches = mutableListOf<String>()

        when (args.size) {
            1 -> matches.addAll(playerManager.getPlayerCompletions(args[0]))
        }

        return matches
    }


}