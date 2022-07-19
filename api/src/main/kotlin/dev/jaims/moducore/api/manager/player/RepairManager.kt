package dev.jaims.moducore.api.manager.player

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface RepairManager {

    /**
     * Method to repair a players item in hand.
     *
     * @param player the player whose item you want to repair
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     */
    fun repair(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Method to repair all things in a players inventory.
     *
     * @param player the player whose item you want to repair
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     */
    fun repairAll(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true)
}