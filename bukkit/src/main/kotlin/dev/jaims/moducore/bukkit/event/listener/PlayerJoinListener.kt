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

package dev.jaims.moducore.bukkit.event.listener

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.config.Warps
import dev.jaims.moducore.bukkit.util.Perm
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import java.util.*

class PlayerJoinListener(private val plugin: ModuCore) : Listener {

    private val fileManager = plugin.api.fileManager
    private val playtimeManager = plugin.api.playtimeManager
    private val storageManager = plugin.api.storageManager

    private var isPermsCached = false

    // called before PlayerJoinEvent
    @EventHandler
    fun PlayerLoginEvent.onLogin() {
    }

    // called after the PlayerLoginEvent
    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        // join message
        if (fileManager.modules[Modules.JOIN_MESSAGE]) {
            joinMessage = fileManager.getString(Lang.JOIN_MESSAGE, player)
        }

        // cache perms (for luckperms)
        if (!isPermsCached) {
            Perm.values().forEach { player.hasPermission(it.permString) }
        }

        // spawn on join
        if (fileManager.modules[Modules.SPAWN]) {
            if (fileManager.config[Config.SPAWN_ON_JOIN]) {
                player.teleport(fileManager.warps[Warps.SPAWN].location)
            }
        }

        // add the player to the join times map
        playtimeManager.joinTimes[player.uniqueId] = Date()

        // load player data
        storageManager.playerDataCache[player.uniqueId] = storageManager.getPlayerData(player.uniqueId)
    }

}