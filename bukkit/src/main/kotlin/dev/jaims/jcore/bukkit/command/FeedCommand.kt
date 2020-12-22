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
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.jcore.bukkit.util.noConsoleCommand
import dev.jaims.jcore.bukkit.util.playerNotFound
import dev.jaims.jcore.bukkit.util.usage
import dev.jaims.mcutils.bukkit.feed
import dev.jaims.mcutils.bukkit.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FeedCommand(private val plugin: JCore) : JCoreCommand {

    override val commandName = "feed"
    override val usage = "/feed [target]"
    override val description = "Feed yourself or a target."

    val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            0 -> {
                // check perms
                if (!Perm.FEED.has(sender)) return false
                // only players
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return false
                }
                sender.feed()
                sender.send(fileManager.getString(Lang.FEED_SUCCESS, sender))
            }
            1 -> {
                // check perms
                if (!Perm.FEED_OTHERS.has(sender)) return false
                // get target
                val target = plugin.api.playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return false
                }
                target.feed()
                if (fileManager.config.getProperty(Config.ALERT_TARGET)) {
                    target.send(fileManager.getString(Lang.FEED_SUCCESS, target))
                }
                sender.send(fileManager.getString(Lang.TARGET_FEED_SUCCESS, target))
            }
            else -> sender.usage(usage, description)
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