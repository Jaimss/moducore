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

package dev.jaims.moducore.bukkit.migration

import dev.jaims.moducore.api.data.LocationHolder
import dev.jaims.moducore.api.data.PlayerData
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Warps
import java.util.*

interface PluginMigrator {

    /**
     * Save all the data to our data.
     */
    fun migrate(plugin: ModuCore) {
        // save the player data
        getAllPlayerData().forEach { (uuid, playerData) ->
            plugin.api.storageManager.setPlayerData(uuid, playerData)
        }
        // save the warps
        getWarps().forEach { (name, locationHolder) ->
            val warps = plugin.api.fileManager.warps
            val modified = warps[Warps.WARPS].toMutableMap()
            modified[name] = locationHolder
            warps[Warps.WARPS] = modified
            warps.save()
        }
        // save the spawn
        val spawnLocation = getDefaultSpawn()
        val warps = plugin.api.fileManager.warps
        warps[Warps.SPAWN] = spawnLocation
        warps.save()
    }

    /**
     * Get all the plugins player data.
     */
    fun getAllPlayerData(): Map<UUID, PlayerData>

    /**
     * Get all the warps the plugin has.
     */
    fun getWarps(): Map<String, LocationHolder>

    /**
     * Get the spawn of the plugin.
     */
    fun getDefaultSpawn(): LocationHolder

}