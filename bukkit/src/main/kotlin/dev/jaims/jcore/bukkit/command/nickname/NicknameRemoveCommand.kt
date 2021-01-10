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

package dev.jaims.jcore.bukkit.command.nickname

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.command.BaseCommand
import dev.jaims.jcore.bukkit.util.Perm
import dev.jaims.jcore.bukkit.util.noConsoleCommand
import dev.jaims.jcore.bukkit.util.playerNotFound
import dev.jaims.jcore.bukkit.util.usage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class NicknameRemoveCommand(override val plugin: JCore) : BaseCommand
{
    override val usage: String = "/unnick [target]"
    override val description: String = "Remove your nickname."
    override val commandName: String = "unnick"

    private val storageManager = plugin.api.storageManager
    private val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager

    override fun execute(sender: CommandSender, args: List<String>, silent: Boolean)
    {
        when (args.size)
        {
            0 ->
            {
                if (!Perm.UNNICK.has(sender)) return
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }
                playerManager.setNickName(sender.uniqueId, null, silent, storageManager)
            }
            1 ->
            {
                if (!Perm.UNNICK_OTHERS.has(sender)) return
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                playerManager.setNickName(target.uniqueId, null, silent, storageManager, sender)
            }
            else -> sender.usage(usage, description)
        }
        return
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>
    {
        val completions = mutableListOf<String>()

        when (args.size)
        {
            1 -> completions.addAll(playerManager.getPlayerCompletions(args[0]))
        }

        return completions
    }
}