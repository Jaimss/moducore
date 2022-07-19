package dev.jaims.moducore.api.manager.location

import dev.jaims.moducore.api.data.LocationHolder
import org.bukkit.entity.Player

interface SpawnManager {

    /**
     * Set the spawn of the server.
     *
     * @param locationHolder the [LocationHolder] of spawn
     * @param player the player who set the location, or null
     */
    fun setSpawn(locationHolder: LocationHolder, player: Player?)

    /**
     * Get the spawn location.
     *
     * @return a [LocationHolder]
     */
    fun getSpawn(): LocationHolder

}