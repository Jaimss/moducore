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

package dev.jaims.moducore.bukkit.api.manager

import com.google.gson.Gson
import dev.jaims.hololib.core.Hologram
import dev.jaims.hololib.core.HololibManager
import dev.jaims.hololib.gson.hololibGsonBuilder
import dev.jaims.moducore.api.manager.HologramManager
import dev.jaims.moducore.bukkit.ModuCore
import org.bukkit.Bukkit
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.text.DateFormat

class DefaultHologramManager(private val plugin: ModuCore) : HologramManager {

    private val fileManager: BukkitFileManager by lazy { plugin.api.bukkitFileManager }

    val saveTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
        hololibManager.cachedHolograms.forEach { holo -> saveHologram(holo.name, holo) }
    }, 0, 20 * 60)

    val updateTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
        hololibManager.cachedHolograms.forEach { holo -> holo.update() }
    }, 0, 20 * 2)

    override val gson: Gson = hololibGsonBuilder
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .setDateFormat(DateFormat.FULL)
        .create()

    override val hololibManager = HololibManager(plugin)

    init {
        hololibManager.lineTransformation = { player, content -> content.colorize(player) }
        hololibManager.cachedHolograms.addAll(getAllHolograms().values)
    }

    /**
     * Get all holograms.
     */
    override fun getAllHolograms(): Map<String, Hologram> {
        val results = mutableMapOf<String, Hologram>()
        File("${plugin.dataFolder}/hologram/").walk().filter { !it.isDirectory }.forEach { file ->
            results[file.nameWithoutExtension] = getHologram(file.nameWithoutExtension) ?: return@forEach
        }
        return results
    }

    /**
     * Get a hologram from the storage.
     */
    override fun getHologram(name: String): Hologram? {
        val file = File(plugin.dataFolder, "hologram/$name.json")
        if (!file.exists()) return null
        val reader = FileReader(file, StandardCharsets.UTF_16)
        val hologram = gson.fromJson(reader, Hologram::class.java)
        reader.close()
        return hologram
    }

    /**
     * save a hologram
     */
    override fun saveHologram(name: String, hologram: Hologram) {
        val file = File(plugin.dataFolder, "hologram/$name.json")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        val writer = FileWriter(file, StandardCharsets.UTF_16)
        gson.toJson(hologram, Hologram::class.java, writer)
        writer.close()
    }

    /**
     * Delete a holo
     */
    override fun deleteHologram(hologram: Hologram) {
        hologram.despawn()
        File(plugin.dataFolder, "hologram/${hologram.name}.json").delete()
    }

    /**
     * Rename a hologram
     */
    override fun rename(hologram: Hologram, newName: String) {
        val file = File(plugin.dataFolder, "hologram/${hologram.name}.json")
        hologram.name = newName
        file.renameTo(File(plugin.dataFolder, "hologram/${hologram.name}.json"))
    }

}