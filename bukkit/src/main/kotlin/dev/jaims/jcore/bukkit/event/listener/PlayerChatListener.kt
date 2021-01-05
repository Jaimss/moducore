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

package dev.jaims.jcore.bukkit.event.listener

import dev.jaims.jcore.api.event.JCoreAsyncChatEvent
import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.config.Config
import dev.jaims.jcore.bukkit.config.Lang
import dev.jaims.jcore.bukkit.config.Modules
import dev.jaims.jcore.bukkit.util.Perm
import dev.jaims.mcutils.bukkit.async
import dev.jaims.mcutils.bukkit.send
import me.mattstudios.mfmsg.base.MessageOptions
import me.mattstudios.mfmsg.base.internal.Format
import me.mattstudios.mfmsg.bukkit.BukkitMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChatListener(private val plugin: JCore) : Listener
{

    private val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager

    @EventHandler(ignoreCancelled = true)
    fun AsyncPlayerChatEvent.onChat()
    {
        // if they want to do the chat, we let them
        if (!fileManager.modules.getProperty(Modules.CHAT)) return

        // use our own chat event
        isCancelled = true

        // make sure it is run async
        if (!isAsynchronous)
        {
            async(plugin) { handleChat() }
            return
        }
        handleChat()
    }

    private fun AsyncPlayerChatEvent.handleChat()
    {

        val originalMessage = message

        // chat ping for all online players
        Bukkit.getOnlinePlayers().forEach {
            if (message.contains(fileManager.getString(Config.CHATPING_ACTIVATOR, it, fileManager.config)))
            {
                message = message.replace(
                    fileManager.getString(Config.CHATPING_ACTIVATOR, it, fileManager.config),
                    fileManager.getString(Config.CHATPING_FORMAT, it, fileManager.config)
                )
            }
        }

        // tell console the message was sent
        Bukkit.getConsoleSender().send(fileManager.getString(Lang.CHAT_FORMAT, player) + message)

        // setup markdown chat based on permissions
        val options = MessageOptions.builder().removeFormat(*Format.ALL.toTypedArray())
        if (Perm.CHAT_MK_BOLD.has(player, false)) options.addFormat(Format.BOLD, Format.LEGACY_BOLD)
        if (Perm.CHAT_MK_ITALIC.has(player, false)) options.addFormat(Format.ITALIC, Format.LEGACY_ITALIC)
        if (Perm.CHAT_MK_STRIKETHROUGH.has(player, false)) options.addFormat(Format.STRIKETHROUGH, Format.LEGACY_STRIKETHROUGH)
        if (Perm.CHAT_MK_UNDERLINE.has(player, false)) options.addFormat(Format.UNDERLINE, Format.LEGACY_UNDERLINE)
        if (Perm.CHAT_MK_OBFUSCATED.has(player, false)) options.addFormat(Format.OBFUSCATED, Format.LEGACY_OBFUSCATED)
        if (Perm.CHAT_MK_COLOR.has(player, false)) options.addFormat(Format.COLOR)
        if (Perm.CHAT_MK_HEX.has(player, false)) options.addFormat(Format.HEX)
        if (Perm.CHAT_MK_GRADIENT.has(player, false)) options.addFormat(Format.GRADIENT)
        if (Perm.CHAT_MK_RAINBOW.has(player, false)) options.addFormat(Format.RAINBOW)
        if (Perm.CHAT_MK_ACTIONS.has(player, false)) options.addFormat(*Format.ACTIONS.toTypedArray())

        // set the final message
        val finalMessage = BukkitMessage.create(options.build()).parse(fileManager.getString(Lang.CHAT_FORMAT, player) + message)

        // call the event and accept if it is cancelled
        val jCoreAsyncChatEvent = JCoreAsyncChatEvent(player, originalMessage, finalMessage, recipients)
        plugin.server.pluginManager.callEvent(jCoreAsyncChatEvent)
        // cancellable
        if (jCoreAsyncChatEvent.isCancelled) return

        // send the message to all recipients.
        recipients.forEach(finalMessage::sendMessage)
    }

}