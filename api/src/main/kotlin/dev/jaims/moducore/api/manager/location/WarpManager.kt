package dev.jaims.moducore.api.manager.location

import dev.jaims.moducore.api.data.LocationHolder

interface WarpManager {

    /**
     * @return a Map of all the warp names and their location
     */
    fun getAllWarps(): Map<String, LocationHolder>

    /**
     * Set a warp
     * @param name the name of the warp
     * @param locationHolder the [LocationHolder]
     */
    fun setWarp(name: String, locationHolder: LocationHolder)

    /**
     * Delete a warp.
     * @param name the name of the warp
     *
     * @return false if the warp didn't exist, true if it did
     */
    fun deleteWarp(name: String): Boolean

    /**
     * @param name the name of the warp
     * @return the [LocationHolder] or null if the warp doesn't exist
     */
    fun getWarp(name: String): LocationHolder? = getAllWarps().mapKeys { it.key.lowercase() }[name.lowercase()]

}