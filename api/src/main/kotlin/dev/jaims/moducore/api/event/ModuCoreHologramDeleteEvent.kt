package dev.jaims.moducore.api.event

import dev.jaims.hololib.core.Hologram
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Called when a hologram is deleted.
 *
 * @param player the player who deleted it
 * @param hologram the hologram deleted.
 */
class ModuCoreHologramDeleteEvent(val player: Player, val hologram: Hologram) : Event() {

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