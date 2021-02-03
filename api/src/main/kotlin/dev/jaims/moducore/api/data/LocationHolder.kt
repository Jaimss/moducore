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

package dev.jaims.moducore.api.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

/**
 * A Location Wrapper for Config Files
 *
 * @param worldName the name of the world
 * @param x the x coordinate
 * @param y the y coordinate
 * @param z the z coordinate
 * @param yaw the yaw
 * @param pitch the pitch
 */
data class LocationHolder(
    var worldName: String = "world",
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var yaw: Float = 0f,
    var pitch: Float = 0f
) {
    companion object {
        @JvmStatic
        fun from(location: Location): LocationHolder {
            return LocationHolder(location.world!!.name, location.x, location.y, location.z, location.yaw, location.pitch)
        }
    }

    /**
     * Get a [Location] from a location holder
     */
    val location: Location
        get() = Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch)

    val world: World
        get() = location.world!!
}
