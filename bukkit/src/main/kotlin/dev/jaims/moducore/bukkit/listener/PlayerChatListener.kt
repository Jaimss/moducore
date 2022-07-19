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

import dev.jaims.moducore.api.event.ModuCoreAsyncChatEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.chat.ChatManager
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.async
import dev.jaims.moducore.bukkit.func.langParsed
import dev.jaims.moducore.bukkit.func.placeholders
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.miniToComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * The player chat listener
 *
 * Unfortunately we have to use [AsyncPlayerChatEvent] to have Spigot compatability
 */
class PlayerChatListener(private val plugin: ModuCore) : Listener {

    private val fileManager = plugin.api.bukkitFileManager
    private val chatManager = ChatManager()

    private val legacySerializer = LegacyComponentSerializer.legacySection()

    /**
     * Handle the chat event with our chat event
     */
    @EventHandler(ignoreCancelled = true)
    fun AsyncPlayerChatEvent.onChat() {
        // if they want to do the chat with another plugin, we let them
        if (!fileManager.modules[Modules.CHAT]) return

        isCancelled = true

        // make sure it is run async
        if (!isAsynchronous) {
            async { handleChat() }
            return
        }
        handleChat()
    }

    private fun AsyncPlayerChatEvent.handleChat() {
        val originalMessage = message

        // chat ping for all online players
        val mentionedPlayers = buildSet<Player> {
            val activator = fileManager.config[Config.CHATPING_ACTIVATOR].langParsed
            val format = fileManager.config[Config.CHATPING_FORMAT].langParsed
            Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
                // continue if the message doesn't contain the activator
                if (!message.contains(activator.placeholders(onlinePlayer))) return@forEach

                message = message.replace(activator.placeholders(onlinePlayer), format.placeholders(onlinePlayer))
                // they will get a ping noise if the player has pings enabled
                add(onlinePlayer)
            }
        }

        val playersToPing = mentionedPlayers
            .filter { plugin.api.storageManager.loadPlayerData(it.uniqueId).join().chatPingsEnabled }
            .toMutableSet()

        // set the final message
        val playerData = plugin.api.storageManager.loadPlayerData(player.uniqueId).join()
        val chatColor = playerData.chatColor ?: ""

        // chat prefix from config
        val chatPrefix: Component = plugin.api.bukkitFileManager.lang[Lang.CHAT_FORMAT]
            .langParsed
            .placeholders(player)
            .miniStyle()
            .miniToComponent()

        // the players message based on permissions
        val playerMessage: Component = chatManager.getMessage(
            chatManager.getAllowedTags(player),
            chatManager.getAllowedDecorations(player),
            Permissions.CHAT_URL.has(player, false),
            chatColor + message
        )

        val finalMessageComponent = chatPrefix.append(playerMessage)

        // call the event and accept if it is cancelled
        val moduCoreAsyncChatEvent =
            ModuCoreAsyncChatEvent(
                player,
                originalMessage,
                finalMessageComponent,
                recipients,
                mentionedPlayers,
                playersToPing
            )
        plugin.server.pluginManager.callEvent(moduCoreAsyncChatEvent)
        // cancellable
        if (moduCoreAsyncChatEvent.isCancelled) return

        // set the spigot event message
        val finalMessageString = legacySerializer.serialize(moduCoreAsyncChatEvent.message)
        message = finalMessageString

        // ping players
        playersToPing.forEach { player ->
            try {
                val sound = Sound.valueOf(fileManager.config[Config.CHATPING_SOUND_NAME])
                val pitch = fileManager.config[Config.CHATPING_SOUND_PITCH]
                val volume = fileManager.config[Config.CHATPING_SOUND_VOLUME]
                player.playSound(player.location, sound, volume, pitch)
            } catch (ignored: IllegalArgumentException) {
                plugin.logger.warning("ChatPing Sound is Invalid! Edit this in the config.")
            }
        }

        // send the message to all recipients.
        plugin.server.onlinePlayers
            .filter { player -> player.uniqueId in moduCoreAsyncChatEvent.recipients.map { it.uniqueId } }
            .forEach { plugin.audience.player(it).sendMessage(moduCoreAsyncChatEvent.message) }
        // send message to console
        plugin.audience.sender(plugin.server.consoleSender).sendMessage(moduCoreAsyncChatEvent.message)
    }

}