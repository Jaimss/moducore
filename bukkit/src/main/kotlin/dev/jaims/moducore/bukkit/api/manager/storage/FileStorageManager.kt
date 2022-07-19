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

package dev.jaims.moducore.bukkit.api.manager.storage

import dev.jaims.moducore.api.data.PlayerData
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.func.async
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FileStorageManager(private val plugin: ModuCore) : StorageManager() {

    override val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override val playerDataCache = mutableMapOf<UUID, PlayerData>()

    override var updateTask = async(60 * 20, 60 * 20) {
        bulkSave(playerDataCache)
    }

    private fun getStorageFile(uuid: UUID): File {
        return File(plugin.dataFolder, "data/$uuid.json")
    }

    /**
     * Update all the player data in the playerdata map.
     */
    override fun bulkSave(bulkData: Map<UUID, PlayerData>) {
        bulkData.forEach {
            savePlayerData(it.key, it.value)
        }
    }

    /**
     * Return all the player data
     */
    override fun loadAllData(): CompletableFuture<List<PlayerData>> {
        return CompletableFuture.supplyAsync({
            val results = mutableListOf<PlayerData>()
            File("${plugin.dataFolder}/data/").walk().forEach { file ->
                // the if is a bad solution for detecting if the result is the folder itself
                // or a file in the folder. File#walk gives the folder itself and all the files.
                // test .filter { !it.isDirectory }
                if (file.name.contains("data/"))
                // #join is fine, this is already async
                    results.add(loadPlayerData(file).join())
            }
            results
        }, executorService)
    }

    private fun loadPlayerData(file: File): CompletableFuture<PlayerData> {
        return CompletableFuture.supplyAsync({
            val reader = FileReader(file)
            val playerData = gson.fromJson(reader, PlayerData::class.java)
            reader.close()
            playerData
        }, executorService)
    }

    override fun loadPlayerData(uuid: UUID): CompletableFuture<PlayerData> {
        val file = getStorageFile(uuid)
        if (!file.exists()) savePlayerData(uuid, PlayerData())

        val cached = playerDataCache[uuid]
        if (cached != null) return CompletableFuture.completedFuture(cached)

        return loadPlayerData(file)
    }

    override fun unloadPlayerData(uuid: UUID) {
        val playerData = playerDataCache.remove(uuid)
        if (playerData != null) savePlayerData(uuid, playerData)
    }

    /**
     * Gets the [PlayerData] for a player. PlayerData is stored in a file.
     */
    override fun getPlayerData(uuid: UUID): PlayerData? {
        // get from cache if it exists
        return playerDataCache[uuid]
    }

    /**
     * Set playerdata
     */
    override fun savePlayerData(uuid: UUID, playerData: PlayerData) {
        executorService.execute {
            val file = getStorageFile(uuid)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val writer = FileWriter(file)
            gson.toJson(playerData, writer)
            writer.close()
        }
    }

}