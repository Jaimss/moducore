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

import com.google.gson.Gson
import dev.jaims.moducore.api.hologram.Hologram
import dev.jaims.moducore.api.hologram.HologramLine
import dev.jaims.moducore.api.hologram.HologramPage
import org.bukkit.Location

interface HologramManager {

    val gson: Gson

    /**
     * Get all holograms.
     *
     * @return a map of the name and the hologram
     */
    fun getAllHolograms(): Map<String, Hologram>

    /**
     * Get a hologram from the storage.
     *
     * @param name case insensitive name of the hologram
     *
     * @return the [Hologram] or null
     */
    fun getHologram(name: String): Hologram?

    /**
     * Create a hologram. Will generate it, add it to the storage and spawn it at the location given.
     *
     * @param name the name of the hologram
     * @param location the location to create it at. (location of the first line)
     * @param pages each "vararg" is its own page. listOf("blah"), listOf("blah", "blah") would be two pages, first with one line, second with two
     */
    fun createHologram(name: String, location: Location, vararg pages: List<String>): Hologram
}
