package dev.jaims.moducore.api.event.command.nickname

import dev.jaims.moducore.api.event.util.ModuCoreEvent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Called when a player removes their nickname.
 *
 * @param oldName the oldname of the player
 * @param player the player
 * @param executor the command sender or null if the player did this to themselves
 */
class ModuCoreNicknameRemoveEvent(
    val oldName: String,
    val player: Player,
    val executor: CommandSender?
) : ModuCoreEvent()