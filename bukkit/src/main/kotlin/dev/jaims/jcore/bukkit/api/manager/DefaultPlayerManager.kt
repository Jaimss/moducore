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

package dev.jaims.jcore.bukkit.api.manager

import dev.jaims.jcore.api.manager.PlayerManager
import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.config.Config
import dev.jaims.jcore.bukkit.config.FileManager
import dev.jaims.jcore.bukkit.config.Lang
import dev.jaims.jcore.bukkit.util.Perm
import dev.jaims.jcore.bukkit.util.repair
import dev.jaims.mcutils.bukkit.send
import dev.jaims.mcutils.common.InputType
import dev.jaims.mcutils.common.getInputType
import dev.jaims.mcutils.common.getName
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.roundToInt

class DefaultPlayerManager(private val plugin: JCore) : PlayerManager
{

    private val fileManager: FileManager by lazy { plugin.api.fileManager }

    /**
     * Get a target player
     */
    override fun getTargetPlayer(input: String): Player?
    {
        if (input.getInputType() == InputType.NAME)
        {
            return Bukkit.getPlayer(input)
        }
        return Bukkit.getPlayer(UUID.fromString(input))
    }

    /**
     * Set a players flyspeed
     */
    override fun setFlySpeed(player: Player, speed: Int, executor: CommandSender?, sendMessage: Boolean)
    {
        if (speed < 0 || speed > 10) throw IllegalArgumentException("Speed can not be below 0 or greater than 10!")
        player.flySpeed = (speed.toDouble() / 10.0).toFloat()
        if (sendMessage)
        {
            sendNullExecutor(player, executor, Lang.FLYSPEED_SUCCESS, Lang.FLYSPEED_SUCCESS_TARGET)
        }
    }

    /**
     * Set a players walkspeed
     */
    override fun setWalkSpeed(player: Player, speed: Int, executor: CommandSender?, sendMessage: Boolean)
    {
        if (speed < 0 || speed > 10) throw IllegalArgumentException("Speed can not be below 0 or greater than 10!")
        player.walkSpeed = ((speed.toDouble() / 2.0).roundToInt() * 0.2).toFloat()
        if (sendMessage)
        {
            sendNullExecutor(player, executor, Lang.WALKSPEED_SUCCESS, Lang.WALKSPEED_SUCCESS_TARGET)
        }
    }

    /**
     * get a list of completions
     */
    override fun getPlayerCompletions(input: String): MutableList<String>
    {
        val completions = mutableListOf<String>()
        for (p in Bukkit.getOnlinePlayers())
        {
            val name = getName(p.uniqueId)
            if (name.contains(input, ignoreCase = true)) completions.add(name)
        }
        return completions
    }

    /**
     * Condense the logic to send a message when the executor of the message is potentially null, and deal with the possible
     * alert target in the config.
     */
    private fun sendNullExecutor(player: Player, executor: CommandSender?, message: Property<String>, executorMessage: Property<String>)
    {
        if (executor == null)
        {
            player.send(fileManager.getString(message, player))
        } else
        {
            if (fileManager.config.getProperty(Config.ALERT_TARGET))
            {
                player.send(fileManager.getString(message, player))
            }
        }
        executor?.send(fileManager.getString(executorMessage, player))
    }

    /**
     * Change a players gamemode to a new gamemode.
     */
    override fun changeGamemode(player: Player, newGameMode: GameMode, executor: CommandSender?, sendMessage: Boolean)
    {
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
        val fileManager = plugin.api.fileManager
        val old = player.gameMode
        when (executor)
        {
            null ->
            {
                if (!(gamemodePermMap[newGameMode] ?: error("Invalid Gamemode")).has(player, sendNoPerms = false)) return
                player.gameMode = newGameMode
                player.send(
                    fileManager.getString(Lang.GAMEMODE_CHANGED, player)
                        .replace("{new}", newGameMode.name.toLowerCase())
                )
            }
            else ->
            {
                if (!(gamemodeTargetPermMap[newGameMode] ?: error("Invalid Gamemode")).has(player, sendNoPerms = false)) return
                player.gameMode = newGameMode
                if (fileManager.config.getProperty(Config.ALERT_TARGET))
                {
                    player.send(
                        fileManager.getString(Lang.GAMEMODE_CHANGED, player)
                            .replace("{new}", newGameMode.name.toLowerCase())
                    )
                }
                executor.send(
                    fileManager.getString(Lang.TARGET_GAMEMODE_CHANGED, player)
                        .replace("{new}", newGameMode.name.toLowerCase())
                        .replace("{old}", old.name.toLowerCase())
                )
            }
        }
    }

    /**
     * Disable a players flight.
     */
    override fun disableFlight(player: Player, executor: CommandSender?, sendMessage: Boolean)
    {
        player.allowFlight = false
        if (sendMessage)
        {
            sendNullExecutor(player, executor, Lang.FLIGHT_DISABLED, Lang.TARGET_FLIGHT_DISABLED)
        }
    }

    /**
     * Enable flight for a player.
     */
    override fun enableFlight(player: Player, executor: CommandSender?, sendMessage: Boolean)
    {
        player.allowFlight = true
        if (sendMessage)
        {
            sendNullExecutor(player, executor, Lang.FLIGHT_ENABLED, Lang.TARGET_FLIGHT_ENABLED)
        }
    }

    /**
     * Method to get a players name.
     * For Now, its just the displayname, but I wanted to add this method so its already being used when I verbosify it
     * to potentially use a database or something for nicknames.
     */
    override fun getName(uuid: UUID): String
    {
        return plugin.server.getPlayer(uuid)?.displayName ?: uuid.getName() ?: "null"
    }

    /**
     * Method to repair a players item in hand.
     */
    override fun repair(player: Player, executor: CommandSender?, sendMessage: Boolean)
    {
        val item = player.inventory.itemInMainHand
        item.repair()
        if (sendMessage)
        {
            sendNullExecutor(player, executor, Lang.REPAIR_SUCCESS, Lang.TARGET_REPAIR_SUCCESS)
        }
    }

    /**
     * Method to repair all things in a players inventory.
     */
    override fun repairAll(player: Player, executor: CommandSender?, sendMessage: Boolean)
    {
        val inv = player.inventory
        val contents = inv.contents.toMutableList()
        contents.addAll(inv.armorContents)
        contents.addAll(inv.extraContents)
        contents.forEach { item ->
            item.repair()
        }
        if (sendMessage)
        {
            sendNullExecutor(player, executor, Lang.REPAIR_ALL_SUCCESS, Lang.TARGET_REPAIR_ALL_SUCCESS)
        }
    }
}