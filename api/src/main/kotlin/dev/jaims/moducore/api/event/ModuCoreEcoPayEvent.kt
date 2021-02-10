package dev.jaims.moducore.api.event

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Event is called whenever a player pays another player.
 *
 * @param to the target player
 * @param from the player who sent money
 * @param amount the amount that was sent. [from] lost this much, [to] gained this much.
 */
class ModuCoreEcoPayEvent(val to: Player, val from: Player, val amount: Double) : Event() {

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