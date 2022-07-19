package dev.jaims.moducore.api.manager.player

import org.bukkit.entity.Player

interface TeleportationManager {

    /**
     * Teleport a player to spawn
     *
     * @param player the player to teleport
     */
    fun teleportToSpawn(player: Player)


    /**
     * Method to warp a player to a warp.
     *
     * @param player the player to warp
     * @param name the name of the warp we want to go to
     *
     * @return true if successful, false if the warp doesn't exist
     */
    fun warpPlayer(player: Player, name: String): Boolean
}