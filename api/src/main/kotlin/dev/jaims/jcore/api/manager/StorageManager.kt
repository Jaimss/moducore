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

package dev.jaims.jcore.api.manager

import com.google.gson.Gson
import java.io.File
import java.util.*

interface StorageManager
{

    val playerData: MutableMap<UUID, PlayerData>

    val gson: Gson

    /**
     * Get all the player data in the storage folder.
     *
     * @return a list of [PlayerData]
     */
    fun getAllData(): List<PlayerData>

    /**
     * Get the [File] that a players storage is in.
     *
     * @param uuid the uuid of the player whose file you want to get
     *
     * @return the [File]
     */
    fun getStorageFile(uuid: UUID): File

    /**
     * Gets the [PlayerData] for a player. PlayerData is stored in a file.
     *
     * @param uuid the uuid of the player.
     *
     * @return the [PlayerData]
     */
    fun getPlayerData(uuid: UUID): PlayerData

    /**
     * Save all the player data cache back to the storage.
     *
     * @param allData the data to save
     */
    fun saveAllData(allData: Map<UUID, PlayerData>)

    /**
     * Set the [PlayerData] for a player.
     *
     * @param uuid the uuid of the player
     * @param playerData the relevant playerdata
     */
    fun setPlayerData(uuid: UUID, playerData: PlayerData)

}

/**
 * A data class that hold the relevant player data for each player.
 *
 * @param nickName the players nickname or null if they don't have one
 */
data class PlayerData(
    var nickName: String? = null
)
