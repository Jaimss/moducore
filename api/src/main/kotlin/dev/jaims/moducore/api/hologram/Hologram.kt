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
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

/**
 * A class containing a Hologram. Do not try to make an instance of this yourself.
 *
 * For creating a hologram, see the hologram manager. If you need to supply something to the
 * [pages] param, supply an empty list. You would then call [Hologram.plus] to add a new page, or [Hologram.set] to set a page. If you try to
 * implement the logic for creating a [HologramPage] and [HologramLine] yourself there is a high change it will not work as you expect. For this
 * reason, you should use the methdos provided as they encompass all the logic for creating the pages, armor stands to hold hologram lines, etc.
 */
interface Hologram {

    /**
     * The list of [HologramPage]s that this hologram contains.
     */
    val pages: MutableList<HologramPage>

    /**
     * The name of the hologram.
     */
    var name: String

    /**
     * The [LocationHolder] of the Hologram.
     */
    var locationHolder: LocationHolder

    /**
     * The [Date] that this hologram was created.
     */
    val creationTimeStamp: Date

    /**
     * A [Location] of the Hologram.
     */
    val location: Location
        get() = locationHolder.location

    /**
     * Change the name of this hologram to [name].
     *
     * @param newName the new name of the hologram.
     */
    fun rename(newName: String)

    /**
     * Teleport the hologram to a new location.
     *
     * @param newLocation the new location of the hologram.
     */
    fun teleport(newLocation: Location)

    /**
     * Delete a whole [Hologram]. Will remove from the storage, the server and hide from all players.
     *
     * @return the [Hologram] removed.
     */
    fun delete(): Hologram

    /**
     * Update a hologram, to refresh the lines, placeholders, etc.
     */
    fun update()

    /**
     * Save the hologram to a storage file.
     */
    fun save()

    /**
     * Add a page to a Hologram.
     *
     * @param lines the lines of the [HologramPage]
     *
     * @return true if the operation completes successfully, false if not
     */
    operator fun plus(lines: List<String>) = set(pages.size, lines)

    /**
     * Set the lines of a [HologramPage]
     *
     * @param index the page to set
     * @param lines the lines to set it to or null if you want to remove the page.
     *
     * @return true if the operation completes successfully, false if not
     */
    operator fun set(index: Int, lines: List<String>?): Boolean

    /**
     * Get what page of the [Hologram] a [Player] is looking at
     *
     * @param player the player to check
     *
     * @return the 0-indexed page the player is viewing or null if they can't see the hologram.
     */
    fun getPage(player: Player): Int? {
        pages.forEachIndexed { index, hologramPage ->
            if (hologramPage.viewers.contains(player.uniqueId)) return index
        }
        return null
    }

    /**
     * Set a [Player] to view a specific page.
     *
     * @param page the page index. if null, they won't be able to see the hologram anymore. If this is outside of the page range, the hologram
     * will be hidden as well.
     * @param player the player to set the page for.
     */
    fun setPage(page: Int?, player: Player)

    /**
     * Set it to the next page for a specific player.
     *
     * @param player the list of players to go to the next page
     *
     * @see [setPage] will use the same behaviors if they are on the last page. The hologram will be hidden from them.
     */
    fun nextPage(vararg player: Player) = player.forEach { setPage(getPage(it)?.plus(1), it) }

    /**
     * Set it to the previous page for a specific player.
     *
     * @param player the players to set it for
     *
     * @see [setPage] will use the same behaviors if they are on the first page. The hologram will be hidden from them.
     */
    fun previousPage(vararg player: Player) = player.forEach { setPage(getPage(it)?.minus(1), it) }

}