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

import dev.jaims.moducore.api.data.give
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.config.Warps
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.SpigotOnlyNoSuchMethod
import dev.jaims.moducore.bukkit.func.langParsed
import dev.jaims.moducore.bukkit.func.placeholders
import dev.jaims.moducore.bukkit.func.suggestPaperWarning
import dev.jaims.moducore.common.message.legacyString
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.miniToComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import java.util.*

class PlayerJoinListener(private val plugin: ModuCore) : Listener {

    private val fileManager = plugin.api.bukkitFileManager
    private val playtimeManager = plugin.api.playtimeManager
    private val storageManager = plugin.api.storageManager
    private val hologramManager = plugin.api.hologramManager
    private val kitmanager = plugin.api.kitManager
    private val playerManager = plugin.api.playerManager

    private var isPermsCached = false

    // called before PlayerJoinEvent
    @EventHandler
    fun PlayerLoginEvent.onLogin() {
        // lockdown
        val group = fileManager.config[Config.LOCKDOWN_GROUP]
        if (group != "none") {
            if (!Permissions.JOIN_LOCKDOWN_GENERAL.has(player, false) { it.replace("<group>", group) }) {
                val lockdownMessage = fileManager.lang[Lang.LOCKDOWN_CANT_JOIN].langParsed.replace("{group}", group)
                val colorized = lockdownMessage.placeholders(player).miniStyle().miniToComponent()
                try {
                    disallow(PlayerLoginEvent.Result.KICK_OTHER, colorized)
                } catch (ignored: SpigotOnlyNoSuchMethod) {
                    plugin.suggestPaperWarning()
                    disallow(PlayerLoginEvent.Result.KICK_OTHER, colorized.legacyString())
                }
                return
            }
        }
    }

    // called after the PlayerLoginEvent
    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        // load player data
        storageManager.loadPlayerData(player.uniqueId).thenAcceptAsync { playerData ->
            storageManager.playerDataCache[player.uniqueId] = playerData
        }

        // kits
        if (!player.hasPlayedBefore()) {
            val kits = fileManager.config[Config.JOIN_KITS].mapNotNull {
                kitmanager.getKit(it) ?: run {
                    plugin.logger.warning("A kit in the join kit list named '$it' was unable to be given!")
                    null
                }
            }
            for (kit in kits) {
                kit.give(player)
            }
        }

        // join message
        if (fileManager.modules[Modules.JOIN_MESSAGE]) {
            val configJoinMessage = fileManager.lang[Lang.JOIN_MESSAGE].langParsed
            val colorized = configJoinMessage.placeholders(player).miniStyle().miniToComponent()
            try {
                joinMessage(colorized)
            } catch (ignored: SpigotOnlyNoSuchMethod) {
                ignored.printStackTrace()
                plugin.suggestPaperWarning()
                joinMessage = colorized.legacyString()
            }
        }

        // show holograms to players that don't see them
        hologramManager.hololibManager.cachedHolograms.forEach { holo ->
            if (holo.getCurrentPageIndex(player) == null) holo.showNextPage(player)
        }

        // cache perms (for luckperms)
        if (!isPermsCached) {
            Permissions.values().forEach { player.hasPermission(it.permString) }
            isPermsCached = true
        }

        // spawn on join
        if (fileManager.modules[Modules.SPAWN]) {
            if (!player.hasPlayedBefore()) {
                player.teleport(fileManager.warps[Warps.SPAWN].location)
            } else if (fileManager.config[Config.SPAWN_ON_JOIN]) {
                player.teleport(fileManager.warps[Warps.SPAWN].location)
            }
        }

        // join commands (console and player)
        if (fileManager.modules[Modules.JOIN_COMMANDS]) {
            val consoleJoinCommands = fileManager.config[Config.CONSOLE_JOIN_COMMANDS].toMutableList()
            val playerJoinCommands = fileManager.config[Config.PLAYER_JOIN_COMMANDS].toMutableList()
            if (!player.hasPlayedBefore()) {
                val consoleFirstJoinCommands = fileManager.config[Config.CONSOLE_FIRST_JOIN_COMMANDS]
                val playerFirstJoinCommands = fileManager.config[Config.PLAYER_FIRST_JOIN_COMMANDS]
                consoleJoinCommands.addAll(consoleFirstJoinCommands)
                playerJoinCommands.addAll(playerFirstJoinCommands)
            }
            val consoleSender = plugin.server.consoleSender
            consoleJoinCommands.forEach {
                plugin.server.dispatchCommand(consoleSender, it.langParsed.placeholders(player))
            }
            playerJoinCommands.forEach { player.chat("/${it.placeholders(player)}") }
        }

        // add the player to the join times map
        playtimeManager.joinTimes[player.uniqueId] = Date()

        // set nickname
        try {
            player.displayName(playerManager.getName(player.uniqueId))
        } catch (ignored: SpigotOnlyNoSuchMethod) {
            plugin.suggestPaperWarning()
            player.setDisplayName(playerManager.getName(player.uniqueId).legacyString())
        }
    }

}
