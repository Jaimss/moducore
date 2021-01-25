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

package dev.jaims.moducore.api.hologram

import dev.jaims.moducore.api.manager.LocationHolder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

/**
 * A class to hold a Hologram Page.
 *
 * Similar to a [Hologram], please DO NOT modify the [lines] or [viewers] yourself. Instead use the [hide] and [show] methods to change who is viewing,
 * and use the [set] and [plus] methods to add / modify the [lines] of the hologram. If you try to do it manually, you will break things. The logic
 * for doing everything safely and error-free is contained in those methods.
 */
interface HologramPage {

    /**
     * The location of the 0 indexed line.
     */
    val locationHolder: LocationHolder

    /**
     * The name of the hologram.
     */
    val name: String

    /**
     * The actual location.
     */
    val location: Location
        get() = locationHolder.location

    /**
     * A list of [HologramLine]s that this page has
     */
    val lines: MutableList<HologramLine>

    /**
     * The set of uuid's who can see this hologram.
     */
    val viewers: MutableSet<UUID>

    /**
     * The set of [Player]s viewing the hologram.
     */
    val viewingPlayers: MutableSet<Player>
        get() = viewers.mapNotNull { Bukkit.getPlayer(it) }.toMutableSet()

    companion object {
        /**
         * The space between the lines.
         */
        const val LINE_SPACE = 0.25
    }

    /**
     * Hide a [HologramPage] from a specific [Player].
     *
     * @param players the players to hide it from.
     */
    fun hide(vararg players: Player)

    /**
     * Show a [HologramPage] to a specific [Player].
     *
     * @param players the players to show it to.
     */
    fun show(vararg players: Player)

    /**
     * Add a line to a hologram.
     *
     * @param content the line to add
     *
     * @return true if success, false if not
     */
    operator fun plus(content: String) = set(lines.size, content)

    /**
     * Set a line for a hologram.
     *
     * @param index the index to set it at
     * @param content the content of the line
     *
     * @return true if success, false if not
     */
    operator fun set(index: Int, content: String): Boolean
}