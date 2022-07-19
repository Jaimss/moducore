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

package dev.jaims.moducore.bukkit.command.pm

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.common.message.rawText
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun sendPrivateMessage(message: String, sender: Player, target: Player, plugin: ModuCore) {
    val targetDisplayname = plugin.api.playerManager.getName(target.uniqueId)
    val senderDisplayname = plugin.api.playerManager.getName(sender.uniqueId)

    sender.send(Lang.PRIVATE_MESSAGE_SENT_FORMAT, target) { it.replace("{message}", message) }
    target.send(Lang.PRIVATE_MESSAGE_RECEIVED_FORMAT, sender) { it.replace("{message}", message) }

    // /reply
    PREVIOUS_SENDER[target.uniqueId] = sender.uniqueId
    PREVIOUS_SENDER[sender.uniqueId] = target.uniqueId

    SPIERS.forEach { uuid ->
        Bukkit.getPlayer(uuid)?.send(Lang.SOCIAL_SPY_FORMAT) {
            it.replace("{sender}", senderDisplayname.rawText())
                .replace("{target}", targetDisplayname.rawText())
                .replace("{message}", message)
        }
    }
}