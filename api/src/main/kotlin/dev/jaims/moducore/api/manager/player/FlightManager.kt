package dev.jaims.moducore.api.manager.player

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface FlightManager {

    /**
     * Disable a players flight.
     *
     * @param player the player whose flight to change
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the person who ran the command or null if the player changed their own flight
     * @param sendMessage true if message should be sent, false if otherwise.
     */
    fun disableFlight(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Enable flight for a player.
     *
     * @param player the player whose flight to change
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the person who ran the command or null if the player changed their own flight
     * @param sendMessage true if message should be sent, false if otherwise.
     */
    fun enableFlight(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Toggle flight for a player.
     * @see enableFlight
     * @see disableFlight
     *
     * @param player the player whose flight to change
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the person who ran the command or null if the player changed their own flight
     * @param sendMessage true if message should be sent, false if otherwise.
     */
    fun toggleFlight(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true) {
        if (player.allowFlight) disableFlight(player, silent, executor, sendMessage)
        else enableFlight(player, silent, executor, sendMessage)
    }

}