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

import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.api.manager.LocationHolder
import dev.jaims.moducore.api.manager.LocationManager
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Warps
import org.bukkit.entity.Player

class DefaultLocationManager(private val plugin: ModuCore) : LocationManager {

    /**
     * Set the spawn of the server.
     */
    override fun setSpawn(locationHolder: LocationHolder, player: Player?) {
        plugin.api.fileManager.warps[Warps.SPAWN] = locationHolder
        plugin.api.fileManager.warps.save()
        player?.send(plugin.api.fileManager.getString(Lang.SPAWN_SET, player))
    }

    /**
     * Get the spawn location.
     */
    override fun getSpawn(): LocationHolder {
        return plugin.api.fileManager.warps[Warps.SPAWN]
    }

    /**
     * @return a Map of all the warp names and their location
     */
    override fun getAllWarps(): Map<String, LocationHolder> {
        return plugin.api.fileManager.warps[Warps.WARPS]
    }

    /**
     * Set a warp
     */
    override fun setWarp(name: String, locationHolder: LocationHolder) {
        getAllWarps().toMutableMap()[name] = locationHolder
        plugin.api.fileManager.warps.save()
    }

    /**
     * Delete a warp.
     */
    override fun deleteWarp(name: String): Boolean {
        val removed = getAllWarps().toMutableMap().remove(name)
        plugin.api.fileManager.warps.save()
        return removed != null
    }
}