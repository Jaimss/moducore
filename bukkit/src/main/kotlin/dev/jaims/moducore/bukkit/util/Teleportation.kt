/*
 * This file is a part of ModuCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaims.moducore.bukkit.util

import com.okkero.skedule.CoroutineTask
import dev.jaims.mcutils.bukkit.event.waitForEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent

fun cancelTeleportationOnMove(player: Player, cooldown: Int, task: CoroutineTask, plugin: ModuCore) {
    plugin.waitForEvent<PlayerMoveEvent>(
        predicate = { it.player.uniqueId == player.uniqueId && it.isFullBlock },
        timeoutTicks = (cooldown * 20).toLong(),
    ) { task.cancel(); player.send(Lang.TELEPORTATION_CANCELLED) }
}

private val PlayerMoveEvent.isFullBlock: Boolean
    get() {
        if (from.pitch != to?.pitch) return false
        if (from.yaw != to?.yaw) return false
        if (from.blockX != to?.blockX) return true
        if (from.blockY != to?.blockY) return true
        if (from.blockZ != to?.blockZ) return true
        return false
    }
