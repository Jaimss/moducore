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

package dev.jaims.moducore.bukkit.listener

import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.config.Warps
import dev.jaims.moducore.bukkit.util.langParsed
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerDeathListener(private val plugin: ModuCore) : Listener {

    @EventHandler
    fun PlayerDeathEvent.onDeath() {
        if (plugin.api.fileManager.modules[Modules.DEATH_MESSAGES]) {
            deathMessage = plugin.api.fileManager.config[Config.DEATH_MESSAGES].random().langParsed.colorize(entity)
        }
    }

    @EventHandler
    fun PlayerRespawnEvent.onRespawn() {
        if (plugin.api.fileManager.modules[Modules.SPAWN]) {
            respawnLocation = if (player.bedSpawnLocation != null) {
                player.bedSpawnLocation!!
            } else {
                plugin.api.fileManager.warps[Warps.SPAWN].location
            }
        }
    }

}