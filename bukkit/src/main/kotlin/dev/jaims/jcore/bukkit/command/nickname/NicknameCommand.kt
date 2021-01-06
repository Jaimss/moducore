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
import dev.jaims.jcore.bukkit.config.Config
import dev.jaims.jcore.bukkit.config.Lang
import dev.jaims.jcore.bukkit.util.Perm
import dev.jaims.jcore.bukkit.util.noConsoleCommand
import dev.jaims.jcore.bukkit.util.playerNotFound
import dev.jaims.jcore.bukkit.util.usage
import dev.jaims.mcutils.bukkit.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NicknameCommand(private val plugin: JCore) : BaseCommand
{
    override val usage: String = "/nick <name> [target]"
    override val description: String = "Set your nickname."
    override val commandName: String = "nickname"

    private val storageManager = plugin.api.storageManager
    private val fileManager = plugin.api.fileManager
    private val playerManager = plugin.api.playerManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        when (args.size)
        {
            1 ->
            {
                if (!Perm.NICKNAME.has(sender)) return true
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return true
                }
                val name = args[0]
                if (!name.isValidNickname())
                {
                    sender.send(fileManager.getString(Lang.NICKNAME_INVALID))
                    return true
                }
                // will never be null
                storageManager.playerData[sender.uniqueId]!!.nickname = name
                sender.send(fileManager.getString(Lang.NICKNAME_SUCCESS))
            }
            2 ->
            {
                if (!Perm.NICKNAME_OTHERS.has(sender)) return true
                val name = args[0]
                if (!name.isValidNickname())
                {
                    sender.send(fileManager.getString(Lang.NICKNAME_INVALID))
                    return true
                }
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return true
                }
                // do it and send success message
                storageManager.playerData[target.uniqueId]?.nickname = name
                sender.send(fileManager.getString(Lang.NICKNAME_SUCCESS_TARGET))
                if (fileManager.config.getProperty(Config.ALERT_TARGET))
                    target.send(fileManager.getString(Lang.NICKNAME_SUCCESS))
            }
            else -> sender.usage(usage, description)
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>
    {
        val completions = mutableListOf<String>()

        when (args.size)
        {
            2 -> completions.addAll(playerManager.getPlayerCompletions(args[1]))
        }

        return completions
    }

    private fun String.isValidNickname(): Boolean
    {
        // check for regex match
        if (!matches("[\\w\\d]{3,16}".toRegex())) return false

        // check if it matches other nicknames
        val nicknames = storageManager.getAllData().mapNotNull { it.nickname?.toLowerCase() }
        return !nicknames.contains(this.toLowerCase())
    }

}