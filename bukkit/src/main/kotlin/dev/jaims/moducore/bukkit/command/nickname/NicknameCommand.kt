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

package dev.jaims.moducore.bukkit.command.nickname

import dev.jaims.mcutils.bukkit.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NicknameCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/nick <name> [target]"
    override val description: String = "Set your nickname."
    override val commandName: String = "nickname"

    private val storageManager = plugin.api.storageManager
    private val fileManager = plugin.api.fileManager
    private val playerManager = plugin.api.playerManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        when (args.size)
        {
            1 ->
            {
                if (!Perm.NICKNAME.has(sender)) return
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }
                val name = args[0]
                if (!name.isValidNickname())
                {
                    sender.send(fileManager.getString(Lang.NICKNAME_INVALID))
                    return
                }
                // will never be null
                playerManager.setNickName(sender.uniqueId, name, props.silent, storageManager)
            }
            2 ->
            {
                if (!Perm.NICKNAME_OTHERS.has(sender)) return
                val name = args[0]
                if (!name.isValidNickname())
                {
                    sender.send(fileManager.getString(Lang.NICKNAME_INVALID))
                    return
                }
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return
                }
                playerManager.setNickName(target.uniqueId, name, props.silent, storageManager, sender)
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>
    {
        val completions = mutableListOf<String>()

        when (args.size)
        {
            2 -> completions.addAll(playerManager.getPlayerCompletions(args[1]))
        }

        return completions
    }

}