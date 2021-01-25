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

package dev.jaims.moducore.api.manager

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import java.util.*

interface HologramManager {

    /**
     * Create a hologram. Will generate it, add it to the storage and spawn it at the location given.
     *
     * @param name the name of the hologram
     * @param location the location to create it at. (location of the first line)
     * @param pages each "vararg" is its own page. listOf("blah"), listOf("blah", "blah") would be two pages, first with one line, second with two
     */
    fun createHologram(name: String, location: Location, vararg pages: List<String>): Hologram

    /**
     * Get a hologram from the storage.
     *
     * @param name case insensitive name of the hologram
     *
     * @return the [Hologram] or null
     */
    fun getHologram(name: String): Hologram?

    /**
     * Delete a hologram. Will delete it from the storage as well as despawn it.
     *
     * @param name the hologram to delete
     */
    fun deleteHologram(name: String)

    /**
     * Update a hologram.
     *
     * @param name the name of the hologram
     * @param hologram the new data
     */
    fun updateHologram(name: String, hologram: Hologram)

    /**
     * Show a hologram page to a player.
     *
     * @param page the page to show
     * @param player the players to show it to
     */
    fun showToPlayer(page: HologramPage?, vararg player: Player)

    /**
     * Hide a hologram page from a player
     *
     * @param page the page to hide
     * @param player the players to hide it from
     */
    fun hideFromPlayer(page: HologramPage?, vararg player: Player)

    /**
     * Get the current page number that a player is looking at.
     *
     * @param hologram the hologram to check
     * @param player the player to get the page for
     *
     * @return the page number or null if the hologram is hidden from the player entirely
     */
    fun getCurrentPage(hologram: Hologram, player: Player): Int? {
        hologram.pages.forEachIndexed { index, hologramPage ->
            if (hologramPage.viewers.contains(player.uniqueId)) return index
        }
        return null
    }

    /**
     * Set the page
     *
     * @param hologram the page to set it for
     * @param page the page to set it to or null to hide the hologram entirely. if this is greater than or less than the range of pages, it will hide it as well
     * @param player the players to set the page for
     */
    fun setPage(hologram: Hologram, page: Int?, vararg player: Player)

    /**
     * Set it to the next page for a specific player.
     *
     * @param hologram the hologram to change for
     * @param player the list of players to go to the next page
     */
    fun nextPage(hologram: Hologram, vararg player: Player) = player.forEach { setPage(hologram, getCurrentPage(hologram, it)?.plus(1), it) }

    /**
     * Set it to the previous page for a specific player.
     *
     * @param hologram the hologram to change for
     * @param player the players to set it for
     */
    fun previousPage(hologram: Hologram, vararg player: Player) =
        player.forEach { setPage(hologram, getCurrentPage(hologram, it)?.minus(1), it) }

}

/**
 * Data class for a hologram. Everything is 0 indexed here. Current page 0 is the first, 1 is the second page, etc.
 *
 * @param pages the list of [HologramPage]s. Size is the total pages.
 * @param location the location of the hologram
 */
data class Hologram(
    val pages: List<HologramPage>,
    val location: LocationHolder,
)

/**
 * Data class for a Hologram Page
 *
 * @param lines a list of strings one for each line
 * @param armorStandIds the list of armorstands associated with this page
 * @param viewers a list of viewers of the armor stand.
 */
data class HologramPage(
    val lines: List<String>,
    val armorStandIds: List<UUID>,
    val viewers: Set<UUID>
) {
    val armorStands = armorStandIds.map { Bukkit.getEntity(it) as ArmorStand }
    val viewingPlayers = viewers.mapNotNull { Bukkit.getPlayer(it) }
}

