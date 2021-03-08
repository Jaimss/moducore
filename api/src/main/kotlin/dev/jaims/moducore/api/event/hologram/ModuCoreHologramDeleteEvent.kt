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

package dev.jaims.moducore.api.event.hologram

import dev.jaims.hololib.core.Hologram
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Called when a hologram is deleted.
 *
 * @param player the player who deleted it
 * @param hologram the hologram deleted.
 */
class ModuCoreHologramDeleteEvent(val player: Player, val hologram: Hologram) : Event() {

    companion object {
        @JvmStatic
        private val HANDLERS_LIST = HandlerList()

        /**
         * Get the handlers
         */
        @JvmStatic
        fun getHandlerList() = HANDLERS_LIST
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }

}