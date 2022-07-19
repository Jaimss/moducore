package dev.jaims.moducore.api.manager.player

import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface GameModeManager {

    /**
     * Change a players gamemode to a new gamemode.
     *
     * @param player The [Player] whose gamemode we are changing.
     * @param newGameMode the new [GameMode] that the player will be.
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor the person who ran the command or null if the player did it to themselves
     * @param sendMessage if true sends messages to players involved, if false it doesn't
     */
    fun changeGamemode(
        player: Player,
        newGameMode: GameMode,
        silent: Boolean,
        executor: CommandSender? = null,
        sendMessage: Boolean = true
    )

}