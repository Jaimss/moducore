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
import dev.jaims.hololib.core.Hologram
import dev.jaims.hololib.core.HololibManager

interface HologramManager {

    val gson: Gson

    val hololibManager: HololibManager

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
     * Save a hologram to the storage.
     *
     * @param name the name of the hologram
     * @param hologram the data of the hologram.
     */
    fun saveHologram(name: String, hologram: Hologram)

    /**
     * Delete a hologram.
     *
     * @param hologram the hologram to delete.
     */
    fun deleteHologram(hologram: Hologram)

    /**
     * Rename a hologram.
     *
     * @param hologram the hologram to rename.
     * @param newName the new name of the hologram
     */
    fun rename(hologram: Hologram, newName: String)

}
