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

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.mcutils.bukkit.util.kill
import dev.jaims.moducore.api.manager.Hologram
import dev.jaims.moducore.api.manager.HologramManager
import dev.jaims.moducore.api.manager.HologramPage
import dev.jaims.moducore.api.manager.LocationHolder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.FileManager
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import java.io.File
import java.io.FileReader
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

    override val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    /**
     * Create a hologram. Will generate it, add it to the storage and spawn it at the location given.
     */
    override fun createHologram(name: String, location: Location, vararg pages: List<String>): Hologram {
        // each page
        val hologramPages = mutableListOf<HologramPage>()
        pages.forEach { page ->
            val lines = mutableListOf<String>()
            val stands = mutableListOf<UUID>()
            // each line in the page
            page.forEachIndexed { index, line ->
                val stand = createArmorStand(location.subtract(0.0, index.toDouble() * LINE_SPACE, 0.0), line)
                lines.add(line)
                stands.add(stand.uniqueId)
            }
            hologramPages.add(HologramPage(lines, stands, mutableSetOf()))
        }
        val hologram = Hologram(hologramPages, LocationHolder.from(location))

        hologram.pages.forEach {
            hideFromPlayer(it, *Bukkit.getOnlinePlayers().toTypedArray())
        }

        saveHologramToFile(name, hologram)

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
                results[file.nameWithoutExtension.replace("hologram/", "")] = getHologramFromFile(file.name) ?: return@forEach
        }
        return results
    }

    /**
     * Get a hologram from the storage.
     */
    override fun getHologramFromFile(name: String): Hologram? {
        val file = File(plugin.dataFolder, "hologram/$name.json")
        if (!file.exists()) return null
        val reader = FileReader(file)
        val hologram = gson.fromJson(reader, Hologram::class.java)
        reader.close()
        return hologram
    }

    /**
     * Save a holo
     */
    override fun saveHologramToFile(name: String, hologram: Hologram) {
        val file = File(plugin.dataFolder, "hologram/$name.json")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        val writer = file.writer()
        gson.toJson(hologram, writer)
        writer.close()
    }

    /**
     * Delet a file
     */
    override fun deleteHologramFile(name: String) {
        val file = File(plugin.dataFolder, "hologram/$name.json")
        file.delete()
    }

    /**
     * Delete a hologram. Will delete it from the storage as well as despawn it.
     */
    override fun deleteHologram(name: String) {
        val hologram = getHologramFromFile(name) ?: return
        // kill all the holograms
        hologram.pages.forEach {
            hideFromPlayer(it, *it.viewingPlayers.toTypedArray())
            it.armorStands.forEach { stand ->
                stand.kill()
            }
        }

        // remove it from the file
        deleteHologramFile(name)
    }

    /**
     * Update a hologram.
     */
    override fun updateHologram(name: String, hologram: Hologram) {
        // the old hologram will be deleted
        val old = getHologramFromFile(name) ?: return
        // get all the viewers of each page
        val viewers = mutableListOf<List<Player>>()
        old.pages.forEach { page ->
            viewers.add(page.viewingPlayers)
        }
        // show each new page to the previous viewers
        hologram.pages.forEachIndexed { index, page ->
            showToPlayer(page, *viewers.getOrElse(index) { emptyList() }.toTypedArray())
        }
        // add the hologram back to the data file
        saveHologramToFile(name, hologram)
    }

    /**
     * Show a hologram page to a player.
     */
    override fun showToPlayer(page: HologramPage?, vararg player: Player) {
        page?.armorStands?.forEachIndexed { index, stand ->
            player.forEach { player ->
                stand.customName = page.lines[index].colorize(player)
                // https://wiki.vg/Protocol#Spawn_Living_Entity
                val packet = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING)
                with(packet) {
                    integers.write(0, stand.entityId)
                    doubles
                        .write(0, stand.location.x)
                        .write(1, stand.location.y)
                        .write(2, stand.location.z)
                }
                protocolManager.sendServerPacket(player, packet)
                page.viewers.add(player.uniqueId)
            }
        }
    }

    /**
     * Hide a hologram page from a player
     */
    override fun hideFromPlayer(page: HologramPage?, vararg player: Player) {
        page?.armorStands?.forEach { stand ->
            player.forEach { player ->
                // https://wiki.vg/Protocol#Destroy_Entities
                val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
                with(packet) {
                    integers.write(0, 1)
                    integerArrays.write(0, arrayOf(stand.entityId).toIntArray())
                }
                protocolManager.sendServerPacket(player, packet)
                page.viewers.remove(player.uniqueId)
            }
        }
    }

    /**
     * Add a line to a hologram.
     */
    override fun addLine(name: String, hologram: Hologram, page: Int, lineIndex: Int, line: String) {
        // get the page or a default
        val hologramPage = hologram.pages.getOrElse(page) { HologramPage(mutableListOf(), mutableListOf(), mutableSetOf()) }
        // add the line
        hologramPage.lines.add(lineIndex, line)

        val location =
            hologramPage.armorStands.getOrNull(lineIndex - 1)?.location?.subtract(0.0, LINE_SPACE, 0.0) ?: hologram.location.location
        // add the armor stand if it doesn't exist
        hologramPage.armorStands.getOrNull(lineIndex) ?: run {
            hologramPage.armorStandIds
                .add(createArmorStand(location, line).uniqueId)
        }

        updateHologram(name, hologram)
    }

    /**
     * Set the page
     */
    override fun setPage(hologram: Hologram, page: Int?, vararg player: Player) {
        // hide all the pages for each player
        hologram.pages.forEach { hideFromPlayer(it, *player) }
        if (page == null) return

        // show the new page to the player if the page wasn't null
        // if it is out of the range or pages, nothing will happen
        showToPlayer(hologram.pages.getOrNull(page), *player)
    }
}