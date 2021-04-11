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

import dev.jaims.moducore.api.data.Kit
import dev.jaims.moducore.api.manager.KitManager
import dev.jaims.moducore.bukkit.ModuCore
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

class DefaultKitManager(val plugin: ModuCore) : KitManager() {
    override val kitCache: MutableList<Kit> = mutableListOf()

    private val kitsConfig: YamlConfiguration
    private val kitsFile = File(plugin.dataFolder, "kits.yml")

    init {
        // create the kits.yml file
        if (!kitsFile.exists()) kitsFile.createNewFile()
        kitsConfig = YamlConfiguration.loadConfiguration(kitsFile)
        kitCache.addAll(getAllKits())
    }

    /**
     * Updates the cache. Usually called when creating or editing kits, as well as on reloads
     */
    override fun reload() {
        // reload kits file
        kitsConfig.load(kitsFile)
        // fill cache
        kitCache.clear()
        kitCache.addAll(getAllKits())
    }

    /**
     * @return a list of all the kits on the server
     */
    override fun getAllKits(): List<Kit> {
        val kitsSection = kitsConfig.getConfigurationSection("kits") ?: return emptyList()
        val kits = mutableListOf<Kit>()
        for (kitName in kitsSection.getKeys(false)) {
            val cooldown = kitsSection.getInt("$kitName.cooldown")
            val playerCommands = kitsSection.getList("$kitName.player_commands")?.mapNotNull {
                if (it is String) {
                    return@mapNotNull it
                }
                plugin.logger.warning("An item (${it.toString()}) was not a valid command!")
                null
            } ?: emptyList()
            val consoleCommands = kitsSection.getList("$kitName.console_commands")?.mapNotNull {
                if (it is String) {
                    return@mapNotNull it
                }
                plugin.logger.warning("An item (${it.toString()}) was not a valid command!")
                null
            } ?: emptyList()
            val items = kitsSection.getList("$kitName.items")?.mapNotNull {
                if (it is ItemStack) {
                    return@mapNotNull it
                }
                println("An Item (${it.toString()}) was not a valid itemStack")
                null
            } ?: emptyList() // return an empty list bc the list of items was not found
            kits.add(Kit(kitName, cooldown, items, consoleCommands, playerCommands))
        }
        return kits
    }

    /**
     * Create a kit with a [name]
     *
     * @param name the name of the kit
     * @param items the items in the kit
     *
     * @return the created kit
     */
    override fun createKit(
        name: String,
        cooldown: Int,
        items: List<ItemStack>,
        consoleCommands: List<String>,
        playerCommands: List<String>
    ): Kit {
        val kit = Kit(name, cooldown, items, consoleCommands, playerCommands)

        kitCache.add(kit)
        saveKit(kit)
        return kit
    }

    /**
     * Set a kit to a new ItemStack
     *
     * @param name the name of the kit to edit
     * @param items the list of items
     *
     * @return the kit in its edited state
     */
    override fun setKit(name: String, cooldown: Int, items: List<ItemStack>, consoleCommands: List<String>, playerCommands: List<String>): Kit? {
        val kit = kitCache.firstOrNull { it.name == name } ?: return null
        kit.cooldown = cooldown
        kit.items = items
        kit.playerCommands = playerCommands
        kit.consoleCommands = consoleCommands

        saveKit(kit)
        return kit
    }

    /**
     * Delete a kit.
     *
     * @param name the name of the kit to delete
     */
    override fun deleteKit(name: String): Kit? {
        val kit = kitCache.firstOrNull { it.name == name } ?: return null
        kitCache.remove(kit)

        kitsConfig.set("kits.$name", null)
        kitsConfig.save(kitsFile)

        return kit
    }

    /**
     * Save a kit (likely to a file storage of some type)
     *
     * @param kit the kit to delete
     */
    override fun saveKit(kit: Kit) {
        kitsConfig.set("kits.${kit.name}.cooldown", kit.cooldown)
        kitsConfig.set("kits.${kit.name}.items", kit.items)
        kitsConfig.set("kits.${kit.name}.player_commands", kit.playerCommands)
        kitsConfig.set("kits.${kit.name}.console_commands", kit.consoleCommands)
        kitsConfig.save(kitsFile)
    }

}