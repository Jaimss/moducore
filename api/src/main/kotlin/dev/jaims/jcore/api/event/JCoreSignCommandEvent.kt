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

import org.bukkit.block.Sign
import org.bukkit.command.CommandSender
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerInteractEvent

/**
 * An event called when a player clicks a sign to run a command.
 *
 * @param sender the player/consolesender that ran the command. you can use an instanceOf check to see whether console or a player ran the command
 * @param command the command that was ran by the player, completely unmodified.
 * @param actualCommand the command that was ran by the player, colorized and with placeholders replaced appropriately
 * @param signClicked the sign the player clicked. can be used to get coordinates/location, etc etc
 * @param interactEvent the rest of the event data if you want it for any reason
 *
 * @sample dev.jaims.jcore.example.listener.SignCommandListener
 */
@Suppress("UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
class JCoreSignCommandEvent(
    val sender: CommandSender,
    val command: String,
    val actualCommand: String,
    val signClicked: Sign,
    val interactEvent: PlayerInteractEvent,
) : Event(), Cancellable
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