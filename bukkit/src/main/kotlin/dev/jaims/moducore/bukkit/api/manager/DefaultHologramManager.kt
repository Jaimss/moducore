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
import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.mcutils.bukkit.util.kill
import dev.jaims.moducore.api.manager.Hologram
import dev.jaims.moducore.api.manager.HologramManager
import dev.jaims.moducore.api.manager.HologramPage
import dev.jaims.moducore.api.manager.LocationHolder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.FileManager
import dev.jaims.moducore.bukkit.config.Holograms
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import java.util.*

class DefaultHologramManager(private val plugin: ModuCore) : HologramManager {

    private val fileManager: FileManager by lazy { plugin.api.fileManager }
    private val protocolManager: ProtocolManager by lazy { plugin.api.protocolManager }

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
            page.forEach { line ->
                val stand = location.world.spawn(location, ArmorStand::class.java) { stand ->
                    with(stand) {
                        setGravity(false)
                        canPickupItems = false
                        customName = line
                        isCustomNameVisible = true
                        isVisible = false
                    }
                }
                lines.add(line)
                stands.add(stand.uniqueId)
            }
            hologramPages.add(HologramPage(lines, stands, setOf()))
        }
        val hologram = Hologram(hologramPages, LocationHolder.from(location))

        hologram.pages.forEach {
            hideFromPlayer(it, *Bukkit.getOnlinePlayers().toTypedArray())
        }

        fileManager.holograms[Holograms.HOLOGRAMS][name] = hologram
        fileManager.holograms.save()

        return hologram
    }

    /**
     * Get a hologram from the storage.
     */
    override fun getHologram(name: String): Hologram? = fileManager.holograms[Holograms.HOLOGRAMS][name]

    /**
     * Delete a hologram. Will delete it from the storage as well as despawn it.
     */
    override fun deleteHologram(name: String) {
        val hologram = getHologram(name) ?: return
        // kill all the holograms
        hologram.pages.forEach {
            hideFromPlayer(it, *it.viewingPlayers.toTypedArray())
            it.armorStands.forEach(Damageable::kill)
        }

        // remove it from the file
        fileManager.holograms[Holograms.HOLOGRAMS].remove(name)
        fileManager.holograms.save()
    }

    /**
     * Update a hologram.
     */
    override fun updateHologram(name: String, hologram: Hologram) {
        // the old hologram will be deleted
        val old = getHologram(name) ?: return
        deleteHologram(name)
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
        fileManager.holograms[Holograms.HOLOGRAMS][name] = hologram
        fileManager.holograms.save()
    }

    /**
     * Show a hologram page to a player.
     */
    override fun showToPlayer(page: HologramPage?, vararg player: Player) {
        page?.armorStands?.forEach { stand ->
            player.forEach { player ->
                stand.customName = stand.customName?.colorize(player)
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
                page.viewers.toMutableSet().add(player.uniqueId)
            }
        }
    }

    /**
     * Hide a hologram page from a player
     */
    override fun hideFromPlayer(page: HologramPage?, vararg player: Player) {
        page?.armorStands?.forEach { stand ->
            player.forEach { player ->
                stand.customName = stand.customName?.colorize(player)
                // https://wiki.vg/Protocol#Destroy_Entities
                val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
                with(packet) {
                    integers.write(0, 1)
                    integerArrays.write(0, arrayOf(stand.entityId).toIntArray())
                }
                protocolManager.sendServerPacket(player, packet)
                page.viewers.toMutableSet().remove(player.uniqueId)
            }
        }
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