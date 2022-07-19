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
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

/**
 * Manages all storage methods. The same for any type of storage
 */
abstract class StorageManager {
    protected abstract val executorService: ExecutorService

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
    abstract val updateTask: BukkitTask

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

    /*
     * Gets the [PlayerData] for a player, they must be online or this will return null.
     *
     * @param uuid the uuid of the player.
     *
     * @return the [PlayerData] or null if the player isn't online
     * @see [loadPlayerData]
     */
    abstract fun getPlayerData(uuid: UUID): PlayerData?

    /**
     * Get all the player data in the storage folder.
     *
     * @return a list of [PlayerData]
     */
    abstract fun loadAllData(): CompletableFuture<List<PlayerData>>

    /**
     * Load [PlayerData] from the storage. If there is a cached value, it will return that
     * resulting in a very quick computation as opposed to fetching it from the database or
     * file storage. Online players should be cached.
     *
     * @return a [CompletableFuture] for the [PlayerData]
     */
    abstract fun loadPlayerData(uuid: UUID): CompletableFuture<PlayerData>

    /**
     * Unload [PlayerData] from the cache
     *
     * Should also save the player data
     */
    abstract fun unloadPlayerData(uuid: UUID)

    /**
     * Save all the player data cache back to the storage.
     *
     * @param bulkData the data to save
     */
    abstract fun bulkSave(bulkData: Map<UUID, PlayerData>)

    /**
     * Set the [PlayerData] for a player.
     *
     * @param uuid the uuid of the player
     * @param playerData the relevant playerdata
     */
    abstract fun savePlayerData(uuid: UUID, playerData: PlayerData)

}
