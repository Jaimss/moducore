package dev.jaims.moducore.bukkit.util

import com.okkero.skedule.CoroutineTask
import dev.jaims.mcutils.bukkit.event.waitForEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent

fun cancelOnMove(player: Player, cooldown: Int, task: CoroutineTask, plugin: ModuCore) {
    plugin.waitForEvent<PlayerMoveEvent>(
        predicate = { it.player.uniqueId == player.uniqueId },
        timeoutTicks = (cooldown * 20).toLong()
    ) { task.cancel(); player.send(Lang.TELEPORTATION_CANCELLED) }
}

