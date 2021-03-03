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

package dev.jaims.moducore.bukkit.api.manager

import dev.jaims.mcutils.bukkit.util.feed
import dev.jaims.mcutils.bukkit.util.heal
import dev.jaims.mcutils.common.InputType
import dev.jaims.mcutils.common.getInputType
import dev.jaims.mcutils.common.getName
import dev.jaims.moducore.api.event.teleport.ModuCoreTeleportToSpawnEvent
import dev.jaims.moducore.api.manager.PlayerManager
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.FileManager
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.isValidNickname
import dev.jaims.moducore.bukkit.util.repair
import dev.jaims.moducore.bukkit.util.send
import io.papermc.lib.PaperLib
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.roundToInt

class DefaultPlayerManager(private val plugin: ModuCore) : PlayerManager {

    private val fileManager: FileManager by lazy { plugin.api.fileManager }
    private val storageManager: StorageManager by lazy { plugin.api.storageManager }

    /**
     * Condense the logic to send a message when the executor of the message is potentially null, and deal with the possible
     * alert target in the config.
     */
    private fun sendNullExecutor(
        player: Player?,
        executor: CommandSender?,
        silent: Boolean,
        message: Property<String>,
        executorMessage: Property<String>
    ) {
        // just send to player
        if (executor == null) {
            player?.send(message, player)
            return
        }
        // send to the player & executor
        if (!silent) {
            player?.send(message, player)
        }
        executor.send(executorMessage, player)
    }

    /**
     * Get a target player
     */
    override fun getTargetPlayer(input: String): Player? {
        if (input.getInputType() == InputType.NAME) {
            val uuidFromNickname =
                storageManager.playerDataCache.filterValues { it.nickName.equals(input, ignoreCase = true) }.keys.firstOrNull()
            if (uuidFromNickname != null) return Bukkit.getPlayer(uuidFromNickname)
            return Bukkit.getPlayer(input)
        }
        return Bukkit.getPlayer(UUID.fromString(input))
    }

    /**
     * Heal a given player
     */
    override fun healPlayer(player: Player, silent: Boolean, executor: CommandSender?, sendMessage: Boolean) {
        player.heal()
        player.feed()
        sendNullExecutor(player, executor, silent, Lang.HEAL_SUCCESS, Lang.TARGET_HEAL_SUCCESS)
    }

    /**
     * Set a players flyspeed
     */
    override fun setFlySpeed(
        player: Player,
        speed: Int,
        silent: Boolean,
        executor: CommandSender?,
        sendMessage: Boolean
    ) {
        if (speed < 0 || speed > 10) throw IllegalArgumentException("Speed can not be below 0 or greater than 10!")
        player.flySpeed = (speed.toDouble() / 10.0).toFloat()
        if (sendMessage) {
            sendNullExecutor(player, executor, silent, Lang.FLYSPEED_SUCCESS, Lang.FLYSPEED_SUCCESS_TARGET)
        }
    }

    /**
     * Set a players nickname.
     */
    override fun setNickName(
        uuid: UUID,
        nickName: String?,
        silent: Boolean,
        storageManager: StorageManager,
        executor: CommandSender?
    ) {
        if (!nickName.isValidNickname()) throw java.lang.IllegalArgumentException("Nickname is invalid!")
        storageManager.getPlayerData(uuid).nickName = nickName
        sendNullExecutor(Bukkit.getPlayer(uuid), executor, silent, Lang.NICKNAME_SUCCESS, Lang.NICKNAME_SUCCESS_TARGET)
    }

    /**
     * Set a players walkspeed
     */
    override fun setWalkSpeed(
        player: Player,
        speed: Int,
        silent: Boolean,
        executor: CommandSender?,
        sendMessage: Boolean
    ) {
        if (speed < 0 || speed > 10) throw IllegalArgumentException("Speed can not be below 0 or greater than 10!")
        player.walkSpeed = ((speed.toDouble() / 2.0).roundToInt() * 0.2).toFloat()
        if (sendMessage) {
            sendNullExecutor(player, executor, silent, Lang.WALKSPEED_SUCCESS, Lang.WALKSPEED_SUCCESS_TARGET)
        }
    }

    /**
     * Teleport a player to spawn
     */
    override fun teleportToSpawn(player: Player) {
        val spawn = plugin.api.locationManager.getSpawn().location
        PaperLib.teleportAsync(player, spawn)
        plugin.server.pluginManager.callEvent(ModuCoreTeleportToSpawnEvent(player, spawn))
    }

    /**
     * get a list of completions
     */
    override fun getPlayerCompletions(input: String): MutableList<String> {
        val completions = mutableListOf<String>()
        for (p in Bukkit.getOnlinePlayers()) {
            val name = p.name
            val nickname = getName(p.uniqueId)
            // add the name to the completions
            if (name.contains(input, ignoreCase = true)) completions.add(name)
            // add the nickname if it isn't their name
            if (nickname == name) continue
            if (nickname.contains(input, ignoreCase = true)) completions.add(nickname)
        }
        return completions
    }

    /**
     * Change a players gamemode to a new gamemode.
     */
    override fun changeGamemode(
        player: Player,
        newGameMode: GameMode,
        silent: Boolean,
        executor: CommandSender?,
        sendMessage: Boolean
    ) {
        // permission maps to make it easier to get the required permission
        val gamemodePermMap = mapOf(
            GameMode.CREATIVE to Permissions.GAMEMODE_CREATIVE,
            GameMode.SURVIVAL to Permissions.GAMEMODE_SURVIVAL,
            GameMode.ADVENTURE to Permissions.GAMEMODE_ADVENTURE,
            GameMode.SPECTATOR to Permissions.GAMEMODE_SPECTATOR
        )
        val gamemodeTargetPermMap = mapOf(
            GameMode.CREATIVE to Permissions.GAMEMODE_CREATIVE_TARGET,
            GameMode.SURVIVAL to Permissions.GAMEMODE_SURVIVAL_TARGET,
            GameMode.ADVENTURE to Permissions.GAMEMODE_ADVENTURE_TARGET,
            GameMode.SPECTATOR to Permissions.GAMEMODE_SPECTATOR_TARGET
        )
        val old = player.gameMode
        when (executor) {
            null -> {
                if (!(gamemodePermMap[newGameMode] ?: error("Invalid Gamemode")).has(
                        player,
                        sendNoPerms = false
                    )
                ) return
                player.gameMode = newGameMode
                player.send(Lang.GAMEMODE_CHANGED, player) { it.replace("{new}", newGameMode.name.toLowerCase()) }
            }
            else -> {
                if (!(gamemodeTargetPermMap[newGameMode] ?: error("Invalid Gamemode")).has(
                        player,
                        sendNoPerms = false
                    )
                ) return
                player.gameMode = newGameMode
                if (!silent) {
                    player.send(Lang.GAMEMODE_CHANGED, player) { it.replace("{new}", newGameMode.name.toLowerCase()) }
                }
                executor.send(Lang.TARGET_GAMEMODE_CHANGED, player) {
                    it.replace("{new}", newGameMode.name.toLowerCase()).replace("{old}", old.name.toLowerCase())
                }
            }
        }
    }

    /**
     * Disable a players flight.
     */
    override fun disableFlight(player: Player, silent: Boolean, executor: CommandSender?, sendMessage: Boolean) {
        player.allowFlight = false
        if (sendMessage) {
            sendNullExecutor(player, executor, silent, Lang.FLIGHT_DISABLED, Lang.TARGET_FLIGHT_DISABLED)
        }
    }

    /**
     * Enable flight for a player.
     */
    override fun enableFlight(player: Player, silent: Boolean, executor: CommandSender?, sendMessage: Boolean) {
        player.allowFlight = true
        if (sendMessage) {
            sendNullExecutor(player, executor, silent, Lang.FLIGHT_ENABLED, Lang.TARGET_FLIGHT_ENABLED)
        }
    }

    /**
     * Feed a player
     */
    override fun feedPlayer(player: Player, silent: Boolean, executor: CommandSender?) {
        player.feed()
        sendNullExecutor(player, executor, silent, Lang.FEED_SUCCESS, Lang.TARGET_FEED_SUCCESS)
    }

    /**
     * Method to get a players name.
     * For Now, its just the displayname, but I wanted to add this method so its already being used when I verbosify it
     * to potentially use a database or something for nicknames.
     */
    override fun getName(uuid: UUID): String {
        return storageManager.getPlayerData(uuid).nickName ?: plugin.server.getPlayer(uuid)?.displayName ?: uuid.getName() ?: "null"
    }

    /**
     * Method to repair a players item in hand.
     */
    override fun repair(player: Player, silent: Boolean, executor: CommandSender?, sendMessage: Boolean) {
        val item = player.inventory.itemInMainHand
        item.repair()
        if (sendMessage) {
            sendNullExecutor(player, executor, silent, Lang.REPAIR_SUCCESS, Lang.TARGET_REPAIR_SUCCESS)
        }
    }

    /**
     * Method to repair all things in a players inventory.
     */
    override fun repairAll(player: Player, silent: Boolean, executor: CommandSender?, sendMessage: Boolean) {
        val inv = player.inventory
        val contents = inv.contents.toMutableList()
        contents.addAll(inv.armorContents)
        contents.addAll(inv.extraContents)
        contents.forEach { item ->
            item.repair()
        }
        if (sendMessage) {
            sendNullExecutor(player, executor, silent, Lang.REPAIR_ALL_SUCCESS, Lang.TARGET_REPAIR_ALL_SUCCESS)
        }
    }

    /**
     * Method to warp a player to a warp.
     */
    override fun warpPlayer(player: Player, name: String): Boolean {
        val warp = plugin.api.locationManager.getWarp(name) ?: return false
        PaperLib.teleportAsync(player, warp.location)
        return true
    }
}