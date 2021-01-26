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

import com.comphenix.protocol.ProtocolManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.jaims.moducore.api.hologram.Hologram
import dev.jaims.moducore.api.hologram.HologramLine
import dev.jaims.moducore.api.hologram.HologramPage
import dev.jaims.moducore.api.manager.HologramManager
import dev.jaims.moducore.api.manager.LocationHolder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.api.manager.hologram.*
import dev.jaims.moducore.bukkit.config.FileManager
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import java.io.File
import java.io.FileReader
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class DefaultHologramManager(private val plugin: ModuCore) : HologramManager {

    private val fileManager: FileManager by lazy { plugin.api.fileManager }
    private val protocolManager: ProtocolManager by lazy { plugin.api.protocolManager }

    companion object {
        const val LINE_SPACE = 0.2
    }

    private fun createArmorStand(location: Location, line: String): ArmorStand {
        return location.world.spawn(location, ArmorStand::class.java) { stand ->
            with(stand) {
                setGravity(false)
                canPickupItems = false
                customName = line
                isCustomNameVisible = true
                isVisible = false
            }
        }
    }

    override val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Hologram::class.java, HologramTypeAdapter())
        .setDateFormat(DateFormat.FULL)
        .create()

    /**
     * Create a hologram. Will generate it, add it to the storage and spawn it at the location given.
     */
    override fun createHologram(name: String, location: Location, vararg pages: List<String>): Hologram {
        val hologram = TextHologram(name, LocationHolder.from(location), Date())
        with(hologram) {
            pages.forEach { page ->
                this + page
            }
            update()
            save()
        }
        return hologram
    }

    /**
     * Get all holograms.
     */
    override fun getAllHolograms(): Map<String, Hologram> {
        val results = mutableMapOf<String, Hologram>()
        File("${plugin.dataFolder}/hologram/").walk().forEach { file ->
            // the if is a bad solution for detecting if the result is the folder itself
            // or a file in the folder. File#walk gives the folder itself and all the files.
            // test .filter { !it.isDirectory }
            if (file.name.contains("hologram/"))
                results[file.nameWithoutExtension.replace("hologram/", "")] = getHologram(file.name) ?: return@forEach
        }
        return results
    }

    /**
     * Get a hologram from the storage.
     */
    override fun getHologram(name: String): Hologram? {
        val file = File(plugin.dataFolder, "hologram/$name.json")
        if (!file.exists()) return null
        val reader = FileReader(file)
        val hologram = gson.fromJson(reader, Hologram::class.java)
        reader.close()
        return hologram
    }
}