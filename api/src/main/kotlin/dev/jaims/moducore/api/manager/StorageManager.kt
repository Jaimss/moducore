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
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import dev.jaims.moducore.api.data.PlayerData
import kotlinx.coroutines.Job
import java.util.*

abstract class StorageManager {
    /**
     * Manages all storage methods. The same for any type of storage
     */

    open val gson: Gson = GsonBuilder()
        .registerTypeAdapter(PlayerData::class.java, InstanceCreator { PlayerData() })
        .setPrettyPrinting()
        .create()

    /**
     * A task that runs every so often to update the cache and save the data back to storage.
     */
    abstract val updateTask: Job

    /**
     * The data cache. This should not be used in most circumstances as the methods allow you to get the data you want
     * in a null safe way.
     */
    abstract val playerDataCache: MutableMap<UUID, PlayerData>

    /**
     * A map of <Discord ID Long, Minecraft UUID>. All other data is stored as [PlayerData] in the [playerDataCache],
     * This is just a quick way to perform a lookup of the users linked account.
     */
    open val linkedDiscordAccounts: Map<Long, UUID>
        get() {
            val links = mutableMapOf<Long, UUID>()
            playerDataCache.forEach { (uuid, playerData) ->
                if (playerData.discordID != null) links[playerData.discordID!!] = uuid
            }
            return links
        }

    /**
     * Get all the player data in the storage folder.
     *
     * @return a list of [PlayerData]
     */
    abstract suspend fun getAllData(): List<PlayerData>

    /**
     * Gets the [PlayerData] for a player. PlayerData is stored in a file.
     *
     * @param uuid the uuid of the player.
     *
     * @return the [PlayerData]
     */
    abstract suspend fun getPlayerData(uuid: UUID): PlayerData

    /**
     * Save all the player data cache back to the storage.
     *
     * @param allData the data to save
     */
    abstract suspend fun saveAllData(allData: Map<UUID, PlayerData>)

    /**
     * Set the [PlayerData] for a player.
     *
     * @param uuid the uuid of the player
     * @param playerData the relevant playerdata
     */
    abstract suspend fun setPlayerData(uuid: UUID, playerData: PlayerData)

}
