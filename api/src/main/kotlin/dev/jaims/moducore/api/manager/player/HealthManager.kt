package dev.jaims.moducore.api.manager.player

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface HealthManager {

    /**
     * Heal a given player
     *
     * @param player the player to heal
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the person who sent the command or null if the player healed themselves
     * @param sendMessage true if messages should be sent
     */
    fun healPlayer(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Feed a player
     *
     * @param player the player to feed
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the person who ran the command or null if they healed themselves
     */
    fun feedPlayer(player: Player, silent: Boolean, executor: CommandSender? = null)
}