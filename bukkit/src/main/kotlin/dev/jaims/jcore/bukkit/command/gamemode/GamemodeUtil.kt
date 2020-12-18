/*
 * This file is a part of JCore, licensed under the MIT License.
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

package dev.jaims.jcore.bukkit.command.gamemode

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.manager.config.Config
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.mcutils.bukkit.send
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Change your own gamemode.
 * if [executor] is not null, someone else changed their gamemode. if it is null, they changed their own
 */
fun changeGamemode(player: Player, new: GameMode, plugin: JCore, executor: CommandSender? = null) {
    val fileManager = plugin.managers.fileManager
    val old = player.gameMode
    when (executor) {
        null -> {
            if (!(gamemodePermMap[new] ?: error("Invalid Gamemode")).has(player, sendNoPerms = false)) return
            player.gameMode = new
            player.send(
                fileManager.getString(Lang.GAMEMODE_CHANGED, player)
                    .replace("{new}", new.name.toLowerCase())
            )
        }
        else -> {
            if (!(gamemodeTargetPermMap[new] ?: error("Invalid Gamemode")).has(player, sendNoPerms = false)) return
            player.gameMode = new
            if (fileManager.config.getProperty(Config.ALERT_TARGET)) {
                player.send(
                    fileManager.getString(Lang.GAMEMODE_CHANGED, player)
                        .replace("{new}", new.name.toLowerCase())
                )
            }
            executor.send(
                fileManager.getString(Lang.TARGET_GAMEMODE_CHANGED, player)
                    .replace("{new}", new.name.toLowerCase())
                    .replace("{old}", old.name.toLowerCase())
            )
        }
    }
}

// permission maps to make it easier to get the required permission
val gamemodePermMap = mapOf(
    GameMode.CREATIVE to Perm.GAMEMODE_CREATIVE,
    GameMode.SURVIVAL to Perm.GAMEMODE_SURVIVAL,
    GameMode.ADVENTURE to Perm.GAMEMODE_ADVENTURE,
    GameMode.SPECTATOR to Perm.GAMEMODE_SPECTATOR
)
val gamemodeTargetPermMap = mapOf(
    GameMode.CREATIVE to Perm.GAMEMODE_CREATIVE_TARGET,
    GameMode.SURVIVAL to Perm.GAMEMODE_SURVIVAL_TARGET,
    GameMode.ADVENTURE to Perm.GAMEMODE_ADVENTURE_TARGET,
    GameMode.SPECTATOR to Perm.GAMEMODE_SPECTATOR_TARGET
)