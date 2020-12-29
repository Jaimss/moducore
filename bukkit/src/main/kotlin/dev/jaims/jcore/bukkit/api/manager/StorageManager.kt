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

package dev.jaims.jcore.bukkit.api.manager

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import dev.jaims.jcore.api.manager.PlayerData
import dev.jaims.jcore.api.manager.IStorageManager
import dev.jaims.jcore.bukkit.JCore
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

class StorageManager(private val plugin: JCore) : IStorageManager {

    override val gson: Gson = GsonBuilder()
        .registerTypeAdapter(PlayerData::class.java, InstanceCreator { PlayerData() }) // defaults
        .setPrettyPrinting()
        .create()

    /**
     * Get the [File] that a players storage is in.
     */
    override fun getStorageFile(uuid: UUID): File {
        return File(plugin.dataFolder, "data/${toString()}.json")
    }

    /**
     * Gets the [PlayerData] for a player. PlayerData is stored in a file.
     */
    override fun getPlayerData(uuid: UUID): PlayerData {
        val file = getStorageFile(uuid)
        if (!file.exists()) file.createNewFile()
        return gson.fromJson(FileReader(file), PlayerData::class.java)
    }

    /**
     * Set playerdata
     */
    override fun setPlayerData(uuid: UUID, playerData: PlayerData) {
        val file = getStorageFile(uuid)
        if (!file.exists()) file.createNewFile()
        gson.toJson(playerData, FileWriter(file))
    }

}