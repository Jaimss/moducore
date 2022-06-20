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

package dev.jaims.moducore.bukkit.listener

import dev.jaims.mcutils.bukkit.func.async
import dev.jaims.mcutils.bukkit.func.colorize
import dev.jaims.moducore.api.event.ModuCoreAsyncChatEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.langParsed
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.perm.Permissions
import me.mattstudios.msg.adventure.AdventureMessage
import me.mattstudios.msg.base.MessageOptions
import me.mattstudios.msg.base.internal.Format
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChatListener(private val plugin: ModuCore) : Listener {

    private val fileManager = plugin.api.fileManager

    /**
     * Handle the chat event with our chat event
     */
    @EventHandler(ignoreCancelled = true)
    suspend fun AsyncPlayerChatEvent.onChat() {
        // if they want to do the chat with another plugin, we let them
        if (!fileManager.modules[Modules.CHAT]) return

        // use our own chat event
        isCancelled = true

        // make sure it is run async
        if (!isAsynchronous) {
            async(plugin) { suspend { handleChat() } }
            return
        }
        handleChat()
    }

    private suspend fun AsyncPlayerChatEvent.handleChat() {
        val originalMessage = message

        // chat ping for all online players
        Bukkit.getOnlinePlayers().forEach {
            if (message.contains(fileManager.config[Config.CHATPING_ACTIVATOR].colorize(player))) {
                message = message.replace(
                    fileManager.config[Config.CHATPING_ACTIVATOR].colorize(it),
                    fileManager.config[Config.CHATPING_FORMAT].colorize(it)
                )
                // ping noise if the player has pings enabled
                if (plugin.api.storageManager.getPlayerData(it.uniqueId).chatPingsEnabled) {
                    try {
                        val sound = Sound.valueOf(fileManager.config[Config.CHATPING_SOUND_NAME])
                        val pitch = fileManager.config[Config.CHATPING_SOUND_PITCH]
                        val volume = fileManager.config[Config.CHATPING_SOUND_VOLUME]
                        it.playSound(it.location, sound, volume, pitch)
                    } catch (ignored: IllegalArgumentException) {
                        plugin.logger.warning("Chatping Sound is Invalid! Edit this in the config.")
                    }
                }
            }
        }

        // tell console the message was sent
        Bukkit.getConsoleSender().send(Lang.CHAT_FORMAT, player) { it + message }

        // setup markdown chat based on permissions
        val options = MessageOptions.builder().removeFormat(*Format.ALL.toTypedArray())
        if (Permissions.CHAT_MK_BOLD.has(player, false)) options.addFormat(Format.BOLD, Format.LEGACY_BOLD)
        if (Permissions.CHAT_MK_ITALIC.has(player, false)) options.addFormat(Format.ITALIC, Format.LEGACY_ITALIC)
        if (Permissions.CHAT_MK_STRIKETHROUGH.has(player, false)) options.addFormat(
            Format.STRIKETHROUGH,
            Format.LEGACY_STRIKETHROUGH
        )
        if (Permissions.CHAT_MK_UNDERLINE.has(player, false)) options.addFormat(
            Format.UNDERLINE,
            Format.LEGACY_UNDERLINE
        )
        if (Permissions.CHAT_MK_OBFUSCATED.has(player, false)) options.addFormat(
            Format.OBFUSCATED,
            Format.LEGACY_OBFUSCATED
        )
        if (Permissions.CHAT_MK_COLOR.has(player, false)) options.addFormat(Format.COLOR)
        if (Permissions.CHAT_MK_HEX.has(player, false)) options.addFormat(Format.HEX)
        if (Permissions.CHAT_MK_GRADIENT.has(player, false)) options.addFormat(Format.GRADIENT)
        if (Permissions.CHAT_MK_RAINBOW.has(player, false)) options.addFormat(Format.RAINBOW)
        if (Permissions.CHAT_MK_ACTIONS.has(player, false)) options.addFormat(*Format.ACTIONS.toTypedArray())

        // set the final message
        val data = plugin.api.storageManager.getPlayerData(player.uniqueId)
        val finalMessage =
            AdventureMessage.create(options.build()).parse(
                fileManager.lang[Lang.CHAT_FORMAT].langParsed.colorize(player) + (data.chatColor ?: "") + message
            )

        // call the event and accept if it is cancelled
        val moduCoreAsyncChatEvent =
            ModuCoreAsyncChatEvent(player, originalMessage, finalMessage, recipients.map { it.uniqueId }.toSet())
        plugin.server.pluginManager.callEvent(moduCoreAsyncChatEvent)
        // cancellable
        if (moduCoreAsyncChatEvent.isCancelled) return

        // send the message to all recipients.
        plugin.server.onlinePlayers.filter { it.uniqueId in moduCoreAsyncChatEvent.recipients }
            .forEach { plugin.audience.player(it).sendMessage(finalMessage) }
    }

}