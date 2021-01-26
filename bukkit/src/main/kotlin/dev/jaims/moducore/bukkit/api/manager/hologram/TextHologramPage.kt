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

package dev.jaims.moducore.bukkit.api.manager.hologram

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.moducore.api.hologram.HologramLine
import dev.jaims.moducore.api.hologram.HologramPage
import dev.jaims.moducore.api.hologram.HologramPage.Companion.LINE_SPACE
import dev.jaims.moducore.api.manager.LocationHolder
import dev.jaims.moducore.bukkit.ModuCore
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class TextHologramPage(override val locationHolder: LocationHolder, override val name: String) : HologramPage {

    companion object {
        val plugin = JavaPlugin.getPlugin(ModuCore::class.java)
    }

    /**
     * A list of [HologramLine]s that this page has
     */
    override val lines: MutableList<HologramLine> = mutableListOf()

    /**
     * The set of uuid's who can see this hologram.
     */
    override val viewers: MutableSet<UUID> = mutableSetOf()

    /**
     * Hide a [HologramPage] from a specific [Player].
     *
     * @param players the players to hide it from.
     */
    override fun hide(vararg players: Player) {
        lines.forEach { line ->
            players.forEach { player ->
                line.armorStand.customName = line.line.colorize(player)
                // https://wiki.vg/Protocol#Spawn_Living_Entity
                val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
                with(packet) {
                    integers.write(0, 1)
                    integerArrays.write(1, arrayOf(line.armorStand.entityId).toIntArray())
                }
                plugin.api.protocolManager.sendServerPacket(player, packet)
                viewers.remove(player.uniqueId)
            }
        }
        plugin.api.hologramManager.getHologram(name)?.save()
    }

    /**
     * Show a [HologramPage] to a specific [Player].
     *
     * @param players the players to show it to.
     */
    override fun show(vararg players: Player) {
        lines.forEach { line ->
            players.forEach { player ->
                line.armorStand.customName = line.line.colorize(player)
                // https://wiki.vg/Protocol#Spawn_Living_Entity
                val packet = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING)
                with(packet) {
                    uuiDs.write(1, line.armorStandId)
                    integers.write(0, line.armorStand.entityId)
                    doubles
                        .write(3, line.armorStand.location.x)
                        .write(4, line.armorStand.location.y)
                        .write(5, line.armorStand.location.z)
                }
                plugin.api.protocolManager.sendServerPacket(player, packet)
                viewers.add(player.uniqueId)
            }
        }
        plugin.api.hologramManager.getHologram(name)?.save()
    }

    /**
     * Set a line for a hologram.
     *
     * @param index the index to set it at
     * @param content the content of the line
     */
    override fun set(index: Int, content: String): Boolean {
        // return if the lines size is less than the index
        if (lines.size < index) return false
        // return if the index is greater than the lines size by more than one
        if (index > lines.size) return false

        val hologramLine = lines.getOrElse(index) {
            TextHologramLine(content, createArmorStand(location.subtract(0.0, index * LINE_SPACE, 0.0), content).uniqueId)
        }

        hologramLine.line = content
        hide(*viewingPlayers.toTypedArray())
        show(*viewingPlayers.toTypedArray())
        plugin.api.hologramManager.getHologram(name)?.save()
        return true
    }

    /**
     * Create an armor stand for a hologram line.
     */
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
}

class HologramPageTypeAdapter : TypeAdapter<HologramPage>() {
    private val gson = GsonBuilder().setPrettyPrinting()
        .registerTypeAdapter(HologramLine::class.java, HologramLineTypeAdapter())
        .create()

    override fun write(out: JsonWriter?, value: HologramPage?) {
        gson.toJson(value, TextHologramPage::class.java, out)
    }

    override fun read(`in`: JsonReader?): HologramPage {
        return gson.fromJson(`in`, TextHologramPage::class.java)
    }

}