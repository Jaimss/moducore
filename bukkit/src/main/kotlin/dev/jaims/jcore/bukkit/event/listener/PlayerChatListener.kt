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

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.jcore.bukkit.manager.config.Modules
import dev.jaims.mcutils.bukkit.send
import me.mattstudios.mfmsg.base.MessageOptions
import me.mattstudios.mfmsg.base.internal.Format
import me.mattstudios.mfmsg.bukkit.BukkitMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerChatListener(private val plugin: JCore) : Listener {

    private val playerManager = plugin.managers.playerManager

    @EventHandler
    fun AsyncPlayerChatEvent.onChat() {
        if (Modules.CHAT_PING.getBool()) {
            Bukkit.getOnlinePlayers().forEach {
                if (message.contains("@${playerManager.getName(it)}")) {
                    message = message.replace("@${playerManager.getName(it)}", "${Lang.NAME.get()}@${playerManager.getName(it)}&r")
                }
            }
        }

        if (!Modules.CHAT_FORMAT.getBool()) return

        isCancelled = true
        Bukkit.getConsoleSender().send(Lang.CHAT_FORMAT.get(player) + message)

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

        val finalMessage = BukkitMessage.create(options.build()).parse(Lang.CHAT_FORMAT.get(player) + message)
        Bukkit.getOnlinePlayers().forEach(finalMessage::sendMessage)
    }

}