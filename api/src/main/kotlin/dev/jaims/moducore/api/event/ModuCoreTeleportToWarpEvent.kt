package dev.jaims.moducore.api.event

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Called when a player warps to a location.
 *
 * @param player the player who warped
 * @param name the name of the warp
 * @param location the location of the warp
 */
class ModuCoreTeleportToWarpEvent(val player: Player, val name: String, val location: Location): Event() {

    companion object {
        @JvmStatic
        private val HANDLERS_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS_LIST
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }
}