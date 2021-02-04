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