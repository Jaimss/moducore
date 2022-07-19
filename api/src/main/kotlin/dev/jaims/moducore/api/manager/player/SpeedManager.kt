package dev.jaims.moducore.api.manager.player

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface SpeedManager {

    /**
     * Set the walkspeed for a player.
     *
     * @param player the player whose speed to change
     * @param speed the new speed of the player between 1 and 10. 1 being the lowest, 10 being the highest.
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the sender of the command if there is a target or null if no target
     * @param sendMessage true if messages should be sent
     */
    fun setWalkSpeed(
        player: Player,
        speed: Int,
        silent: Boolean,
        executor: CommandSender? = null,
        sendMessage: Boolean = true
    )

    /**
     * Set the flyspeed for a player.
     *
     * @param player the player whose speed to change
     * @param speed the new speed of the player between 1 and 10. 1 being the lowest, 10 being the highest.
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the sender of the command if there is a target or null if no target
     * @param sendMessage true if messages should be sent
     */
    fun setFlySpeed(
        player: Player,
        speed: Int,
        silent: Boolean,
        executor: CommandSender? = null,
        sendMessage: Boolean = true
    )
}