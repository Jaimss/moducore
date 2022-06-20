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

package dev.jaims.moducore.bukkit.command

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.playerNotFound
import dev.jaims.moducore.bukkit.func.send
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChatPingToggleCommand(override val plugin: ModuCore) : BaseCommand {

    override val module: Property<Boolean> = Modules.COMMAND_CHAT_PING_TOGGLE
    override val usage: String = "/chatpingtoggle [target]"
    override val description: String = "Toggle the chat ping noise for you or a target."
    override val commandName: String = "chatpingtoggle"
    override val aliases: List<String> = listOf("togglechatping", "cpt")

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        // target modification
        if (args.isNotEmpty()) {
            val playerName = args.first()
            val target = plugin.api.playerManager.getTargetPlayer(playerName) ?: run {
                sender.playerNotFound(playerName)
                return
            }
            val playerData = plugin.api.storageManager.getPlayerData(target.uniqueId)
            playerData.chatPingsEnabled = !playerData.chatPingsEnabled
            val selfMessage = if (playerData.chatPingsEnabled) Lang.CHATPING_ENABLED else Lang.CHATPING_DISABLED
            val targetMessage = if (playerData.chatPingsEnabled) Lang.CHATPING_ENABLED_TARGET
            else Lang.CHATPING_DISABLED_TARGET
            sender.send(targetMessage, target)
            if (!props.isSilent) target.send(selfMessage, target)
            return
        }

        // self modification
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        val playerData = plugin.api.storageManager.getPlayerData(sender.uniqueId)
        playerData.chatPingsEnabled = !playerData.chatPingsEnabled
        val selfMessage = if (playerData.chatPingsEnabled) Lang.CHATPING_ENABLED else Lang.CHATPING_DISABLED
        sender.send(selfMessage, sender)

    }
}