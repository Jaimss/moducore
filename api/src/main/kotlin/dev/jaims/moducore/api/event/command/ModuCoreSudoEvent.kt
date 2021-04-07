package dev.jaims.moducore.api.event.command

import dev.jaims.moducore.api.event.util.ModuCoreEvent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Called when a player is forced to run a command
 * @param command the command run
 * @param player the player who runs the command
 * @param executor the person forcing [player] to run the command
 */
class ModuCoreSudoEvent(
    val command: String,
    val player: Player,
    val executor: CommandSender
) : ModuCoreEvent()