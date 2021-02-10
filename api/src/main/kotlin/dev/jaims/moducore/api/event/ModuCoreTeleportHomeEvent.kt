package dev.jaims.moducore.api.event

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Called when a player teleports home.
 *
 * @param player the player who went home
 * @param name the name of the home
 * @param location the location of their home
 */
class ModuCoreTeleportHomeEvent(val player: Player, val name: String, val location: Location) : Event() {

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