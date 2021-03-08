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

import dev.jaims.moducore.api.data.LocationHolder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

/**
 * Manages all the location methods
 */
interface LocationManager {

    /**
     * Set the spawn of the server.
     *
     * @param locationHolder the [LocationHolder] of spawn
     * @param player the player who set the location, or null
     */
    fun setSpawn(locationHolder: LocationHolder, player: Player?)

    /**
     * Get the spawn location.
     *
     * @return a [LocationHolder]
     */
    fun getSpawn(): LocationHolder

    /**
     * @return a Map of all the warp names and their location
     */
    fun getAllWarps(): Map<String, LocationHolder>

    /**
     * Set a warp
     * @param name the name of the warp
     * @param locationHolder the [LocationHolder]
     */
    fun setWarp(name: String, locationHolder: LocationHolder)

    /**
     * Delete a warp.
     * @param name the name of the warp
     *
     * @return false if the warp didn't exist, true if it did
     */
    fun deleteWarp(name: String): Boolean

    /**
     * @param name the name of the warp
     * @return the [LocationHolder] or null if the warp doesn't exist
     */
    fun getWarp(name: String): LocationHolder? = getAllWarps().mapKeys { it.key.toLowerCase() }[name.toLowerCase()]

}

