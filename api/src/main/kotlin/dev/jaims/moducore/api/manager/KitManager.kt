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

package dev.jaims.moducore.api.manager

import dev.jaims.moducore.api.data.Kit
import org.bukkit.inventory.ItemStack

abstract class KitManager : Reloadable {

    abstract val kitCache: MutableList<Kit>

    /**
     * @return a list of all the kits on the server
     */
    abstract fun getAllKits(): List<Kit>

    /**
     * Method to get a kit from its [name]
     *
     * @param name the name of the kit
     */
    fun getKit(name: String) = kitCache.firstOrNull { it.name == name }

    /**
     * Create a kit with a [name]
     *
     * @param name the name of the kit
     * @param items the items in the kit
     *
     * @return the created kit
     */
    abstract fun createKit(
        name: String,
        cooldown: Int,
        items: List<ItemStack>,
        consoleCommands: List<String>,
        playerCommands: List<String>
    ): Kit

    /**
     * Set a kit to a new ItemStack
     *
     * @param name the name of the kit to edit
     * @param items the list of items
     * @param consoleCommands the console commands
     * @param playerCommands the player commands
     *
     * @return the kit in its edited state
     */
    abstract fun setKit(
        name: String,
        cooldown: Int,
        items: List<ItemStack>,
        consoleCommands: List<String>,
        playerCommands: List<String>
    ): Kit?

    /**
     * Delete a kit.
     *
     * @param name the name of the kit to delete
     *
     * @return the kit that was deleted or null if no kit was found
     */
    abstract fun deleteKit(name: String): Kit?

    /**
     * Save a kit (likely to a file storage of some type)
     *
     * @param kit the kit to delete
     */
    abstract fun saveKit(kit: Kit)

}