package dev.jaims.moducore.api.event.command.nickname

import dev.jaims.moducore.api.event.util.ModuCoreEvent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Sent when a player nicknames.
 * @param oldName the old name or null if they didn't have a nick
 * @param newName the new name
 * @param player the player who nicked
 * @param executor the player or command sender who ran the command to nick this player
 */
class ModuCoreNicknameEvent(
    val oldName: String,
    val newName: String,
    val player: Player,
    val executor: CommandSender?
) : ModuCoreEvent()