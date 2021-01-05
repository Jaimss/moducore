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

package dev.jaims.jcore.api.event

import me.mattstudios.mfmsg.base.internal.MessageComponent
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * The Chat Event that JCore will call if chat is enabled in the modules.
 *
 * @param player the player who sent the message
 * @param originalMessage the original message without markdown formatting
 * @param message the bukkit message with formatting
 * @param recipients the players who will recieve the message
 */
@Suppress("MemberVisibilityCanBePrivate")
class JCoreAsyncChatEvent(
    val player: Player,
    val originalMessage: String,
    var message: MessageComponent,
    var recipients: Set<Player>
) : Event(true), Cancellable
{

    companion object
    {
        private val HANDLERS_LIST = HandlerList()
    }

    override fun getHandlers(): HandlerList
    {
        return HANDLERS_LIST
    }

    private var isCancelled = false

    override fun isCancelled(): Boolean
    {
        return isCancelled
    }

    override fun setCancelled(cancel: Boolean)
    {
        isCancelled = cancel
    }
}