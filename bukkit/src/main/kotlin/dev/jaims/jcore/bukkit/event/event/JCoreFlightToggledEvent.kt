package dev.jaims.jcore.bukkit.event.event

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * @param player - the player whose flight was toggled
 * @param executor - the player who ran the command, or null if the player changed their own flight
 * @param isFlying - the current flight state of the player
 */
class JCoreFlightToggledEvent(player: Player, executor: CommandSender?, isFlying: Boolean) : Event() {

    override fun getHandlers(): HandlerList {
        return HandlerList()
    }

}

