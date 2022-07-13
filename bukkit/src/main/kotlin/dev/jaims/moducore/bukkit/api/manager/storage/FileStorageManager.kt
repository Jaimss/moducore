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

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.github.shynixn.mccoroutine.bukkit.scope
import dev.jaims.moducore.api.data.PlayerData
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.bukkit.ModuCore
import kotlinx.coroutines.*
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

class FileStorageManager(private val plugin: ModuCore) : StorageManager() {

    override val playerDataCache = mutableMapOf<UUID, PlayerData>()

    override var updateTask = plugin.launch(plugin.minecraftDispatcher) {
        withContext(plugin.asyncDispatcher) {
            saveAllData(playerDataCache)
            while (true) {
                delay((60 * 1000).toLong())
            }
        }
    }

    /**
     * Update all the player data in the playerdata map.
     */
    override suspend fun saveAllData(allData: Map<UUID, PlayerData>) {
        allData.forEach {
            setPlayerData(it.key, it.value)
        }
    }

    /**
     * Return all the player data
     */
    override suspend fun getAllData(): List<PlayerData> {
        val results = mutableListOf<PlayerData>()
        File("${plugin.dataFolder}/data/").walk().forEach { file ->
            // the if is a bad solution for detecting if the result is the folder itself
            // or a file in the folder. File#walk gives the folder itself and all the files.
            // test .filter { !it.isDirectory }
            if (file.name.contains("data/"))
                results.add(getPlayerData(file))
        }
        return results
    }

    private fun getStorageFile(uuid: UUID): File {
        return File(plugin.dataFolder, "data/$uuid.json")
    }

    private suspend fun getPlayerData(file: File): PlayerData {
        return plugin.scope.async(Dispatchers.IO) {
            val reader = FileReader(file)
            val data = gson.fromJson(reader, PlayerData::class.java)
            reader.close()
            return@async data
        }.await()
    }

    /**
     * Gets the [PlayerData] for a player. PlayerData is stored in a file.
     */
    override suspend fun getPlayerData(uuid: UUID): PlayerData {
        // get from cache if it exists
        val cachedData = playerDataCache[uuid]
        if (cachedData != null) return cachedData
        // if it's not cached get from the file.
        val file = getStorageFile(uuid)
        if (!file.exists()) setPlayerData(uuid, PlayerData())
        return getPlayerData(file)
    }

    /**
     * Set playerdata
     */
    override suspend fun setPlayerData(uuid: UUID, playerData: PlayerData) {
        return plugin.scope.async(Dispatchers.IO) {
            val file = getStorageFile(uuid)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val writer = FileWriter(file)
            gson.toJson(playerData, writer)
            return@async writer.close()
        }.await()
    }

}