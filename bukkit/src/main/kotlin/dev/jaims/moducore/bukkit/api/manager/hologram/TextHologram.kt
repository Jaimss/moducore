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

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dev.jaims.mcutils.bukkit.util.kill
import dev.jaims.moducore.api.hologram.Hologram
import dev.jaims.moducore.api.hologram.HologramPage
import dev.jaims.moducore.api.hologram.HologramPage.Companion.LINE_SPACE
import dev.jaims.moducore.api.manager.LocationHolder
import dev.jaims.moducore.bukkit.ModuCore
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class TextHologram(
    override var name: String,
    override var locationHolder: LocationHolder,
    override val creationTimeStamp: Date,
) : Hologram {

    override val pages: MutableList<HologramPage> = mutableListOf()

    companion object {
        // get a plugin instance. not a great way really, but it will work
        val plugin: ModuCore = JavaPlugin.getPlugin(ModuCore::class.java)
    }

    /**
     * Change the name of this hologram to [name].
     *
     * @param newName the new name of the hologram.
     */
    override fun rename(newName: String) {
        // rename the file
        val oldFile = File(plugin.dataFolder, "hologram/$name.json")
        this.name = newName
        oldFile.renameTo(File(plugin.dataFolder, "hologram/$name.json"))
        save()
    }

    /**
     * Teleport the hologram to a new location.
     *
     * @param newLocation the new location of the hologram.
     */
    override fun teleport(newLocation: Location) {
        // set the location
        this.locationHolder = LocationHolder.from(newLocation)
        // teleport each page's armor stands to a new location
        pages.forEach { page ->
            page.lines.forEachIndexed { index, line ->
                line.armorStand.teleport(newLocation.subtract(0.0, index * LINE_SPACE, 0.0))
            }
        }
        save()
    }

    /**
     * Delete a whole [Hologram]. Will remove from the storage, the server and hide from all players.
     *
     * @return the [Hologram] removed.
     */
    override fun delete(): Hologram {
        // kill the armor stands
        pages.forEach { page ->
            page.lines.forEach { line ->
                line.armorStand.kill()
            }
        }
        // delete the file
        val file = File(plugin.dataFolder, "hologram/$name.json")
        file.delete()
        // return the hologram
        return this
    }

    /**
     * Update a hologram, to refresh the lines, placeholders, etc.
     */
    override fun update() {
        // for each page, hide it and show it again to update the text / placeholders / whatever else
        pages.forEach { page ->
            val viewers = page.viewingPlayers
            page.hide(*viewers.toTypedArray())
            page.show(*viewers.toTypedArray())
        }
    }

    /**
     * Save the hologram to a storage file.
     */
    override fun save() {
        // get the file & make it if it doesn't exist
        val file = File(plugin.dataFolder, "hologram/$name.json")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        // use a writer to write json to the file
        val writer = file.writer()
        plugin.api.hologramManager.gson.toJson(this, writer)
        writer.close()
    }

    /**
     * Set the lines of a [HologramPage]
     *
     * @param index the page to set
     * @param lines the lines to set it to
     */
    override fun set(index: Int, lines: List<String>?): Boolean {
        if (lines == null) {
            pages.removeAt(index)
            return true
        }
        // return if the page size is less than the index
        if (pages.size < index) return false
        // return if the index is greater than the page size by more than one
        if (index > pages.size) return false
        // the page is valid, now create the armor stand for each line
        val page = TextHologramPage(locationHolder, name)
        lines.forEach { line ->
            page + line
        }
        if (pages.size == index) pages.add(index, page)
        else pages[index] = page
        save()
        return true
    }

    /**
     * Set a [Player] to view a specific page.
     *
     * @param page the page index. if null, they won't be able to see the hologram anymore. If this is outside of the page range, the hologram
     * will be hidden as well.
     * @param player the player to set the page for.
     */
    override fun setPage(page: Int?, player: Player) {
        val pageIndex = getPage(player) ?: return
        val actualPage = pages.getOrNull(pageIndex) ?: return
        actualPage.hide(player)
        if (page == null) return
        val newPage = pages.getOrNull(page) ?: return
        newPage.show(player)
    }
}

class HologramTypeAdapter : TypeAdapter<Hologram>() {
    private val gson = GsonBuilder().setPrettyPrinting()
        .registerTypeAdapter(HologramPage::class.java, HologramPageTypeAdapter())
        .create()

    override fun write(out: JsonWriter?, value: Hologram?) {
        gson.toJson(out, TextHologram::class.java, out)
    }

    override fun read(`in`: JsonReader?): Hologram {
        return gson.fromJson(`in`, TextHologram::class.java)
    }

}