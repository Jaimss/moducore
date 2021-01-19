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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import dev.jaims.moducore.api.manager.PlayerData
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.bukkit.ModuCore
import org.bukkit.scheduler.BukkitTask
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

class FileStorageManager(private val plugin: ModuCore) : StorageManager {

    override val playerDataCache = mutableMapOf<UUID, PlayerData>()

    override var updateTask: BukkitTask = plugin.server.scheduler.runTaskTimerAsynchronously(plugin, Runnable {
        saveAllData(playerDataCache)
    }, 20 * 60, 20 * 60)

    override val gson: Gson = GsonBuilder()
        .registerTypeAdapter(PlayerData::class.java, InstanceCreator { PlayerData() })
        .setPrettyPrinting()
        .create()

    /**
     * Update all the player data in the playerdata map.
     */
    override fun saveAllData(allData: Map<UUID, PlayerData>) {
        allData.forEach {
            setPlayerData(it.key, it.value)
        }
    }

    /**
     * Return all the player data
     */
    override fun getAllData(): List<PlayerData> {
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

    /**
     * Get the [File] that a players storage is in.
     */
    override fun getStorageFile(uuid: UUID): File {
        return File(plugin.dataFolder, "data/$uuid.json")
    }

    /**
     * Get the playerdata for a player from a file.
     */
    private fun getPlayerData(file: File): PlayerData {
        val reader = FileReader(file)
        val data = gson.fromJson(reader, PlayerData::class.java)
        reader.close()
        return data
    }

    /**
     * Gets the [PlayerData] for a player. PlayerData is stored in a file.
     */
    override fun getPlayerData(uuid: UUID): PlayerData {
        // get from cache if it exists
        val cachedData = playerDataCache[uuid]
        if (cachedData != null) return cachedData
        // if its not cached get from the file.
        val file = getStorageFile(uuid)
        if (!file.exists()) setPlayerData(uuid, PlayerData())
        return getPlayerData(file)
    }

    /**
     * Set playerdata
     */
    override fun setPlayerData(uuid: UUID, playerData: PlayerData) {
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