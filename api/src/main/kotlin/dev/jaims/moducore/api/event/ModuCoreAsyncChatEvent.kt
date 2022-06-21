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

package dev.jaims.moducore.api.event

import dev.jaims.moducore.api.event.util.ModuCoreCancellableEvent
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

/**
 * The Chat Event that ModuCore will call if chat is enabled in the modules.
 *
 * @param player the player who sent the message
 * @param originalMessage the original message without markdown formatting
 * @param message the bukkit message with formatting
 * @param recipients the players who will recieve the message
 * @param mentionedPlayers the players that were mentioned in the message using the ChatPing activator
 * @param playersToPing a filtered list of [mentionedPlayers] that only includes those that have chat pings enabled.
 *                      this is who the sound will play for.
 */
@Suppress("MemberVisibilityCanBePrivate")
class ModuCoreAsyncChatEvent(
    val player: Player,
    val originalMessage: String,
    var message: Component,
    var recipients: Set<Player>,
    val mentionedPlayers: Set<Player>,
    var playersToPing: Set<Player>
) : ModuCoreCancellableEvent(true)