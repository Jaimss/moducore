package dev.jaims.moducore.api.event

import dev.jaims.hololib.core.Hologram
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Called when a hologram is created.
 *
 * @param player the player who created the hologram
 * @param hologram the hologram that was created
 */
class ModuCoreHologramCreateEvent(val player: Player, val hologram: Hologram): Event() {

    companion object {
        @JvmStatic
        private val HANDLERS_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS_LIST
    }

    override fun getHandlers(): HandlerList {
        TODO("Not yet implemented")
    }
}