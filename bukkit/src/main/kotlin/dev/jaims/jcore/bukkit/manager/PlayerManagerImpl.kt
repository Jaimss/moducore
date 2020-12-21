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

package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.api.manager.PlayerManager
import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.config.Config
import dev.jaims.jcore.bukkit.manager.config.Lang
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

class PlayerManagerImpl(private val plugin: JCore) : PlayerManager {

    /**
     * return a [Player] with [name]
     */
    internal fun getTargetPlayer(name: String): Player? {
        if (name.getInputType() == InputType.NAME) {
            return Bukkit.getPlayer(name)
        }
        return Bukkit.getPlayer(UUID.fromString(name))
    }

    /**
     * Get a list of players online on the server. Matches their name against a specified [input].
     */
    internal fun getPlayerCompletions(input: String): List<String> {
        val completions = mutableListOf<String>()
        for (p in Bukkit.getOnlinePlayers()) {
            val name = getName(p.uniqueId)
            if (name.contains(input, ignoreCase = true)) completions.add(name)
        }
        return completions
    }

    /**
     * Condense the logic to send a message when the executor of the message is potentially null, and deal with the possible
     * alert target in the config.
     *
     * @param target the target
     * @param executor the person who ran the command or null if it was the player
     * @param targetMessage the [Property] of the target message
     * @param executorMessage the [Property] to send the executor if they are not null
     */
    private fun sendNullExecutor(target: Player, executor: CommandSender?, targetMessage: Property<String>, executorMessage: Property<String>) {
        val fileManager = plugin.api.fileManager
        if (executor == null) {
            target.send(fileManager.getString(targetMessage, target))
        } else {
            if (fileManager.config.getProperty(Config.ALERT_TARGET)) {
                target.send(fileManager.getString(targetMessage, target))
            }
        }
        executor?.send(fileManager.getString(executorMessage, target))
    }

    /**
     * Change a players gamemode to a new gamemode.
     *
     * @param player The [Player] whose gamemode we are changing.
     * @param newGameMode the new [GameMode] that the player will be.
     * @param executor the person who ran the command or null if the player did it to themselves
     * @param sendMessage if true sends messages to players involved, if false it doesn't
     */
    override fun changeGamemode(player: Player, newGameMode: GameMode, executor: CommandSender?, sendMessage: Boolean) {
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
        when (executor) {
            null -> {
                if (!(gamemodePermMap[newGameMode] ?: error("Invalid Gamemode")).has(player, sendNoPerms = false)) return
                player.gameMode = newGameMode
                player.send(
                    fileManager.getString(Lang.GAMEMODE_CHANGED, player)
                        .replace("{new}", newGameMode.name.toLowerCase())
                )
            }
            else -> {
                if (!(gamemodeTargetPermMap[newGameMode] ?: error("Invalid Gamemode")).has(player, sendNoPerms = false)) return
                player.gameMode = newGameMode
                if (fileManager.config.getProperty(Config.ALERT_TARGET)) {
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
     * Method to get a players name.
     * For Now, its just the displayname, but I wanted to add this method so its already being used when I verbosify it
     * to potentially use a database or something for nicknames.
     */
    override fun getName(uuid: UUID): String {
        return plugin.server.getPlayer(uuid)?.displayName ?: uuid.getName() ?: "null"
    }

    /**
     * Method to repair a players item in hand.
     *
     * @param player the player whose item you want to repair
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     */
    override fun repair(player: Player, executor: CommandSender?, sendMessage: Boolean) {
        val item = player.inventory.itemInMainHand
        item.repair()
        if (sendMessage) {
            sendNullExecutor(player, executor, Lang.REPAIR_SUCCESS, Lang.TARGET_REPAIR_SUCCESS)
        }
    }

    /**
     * Method to repair all things in a players inventory.
     *
     * @param player the player whose item you want to repair
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     */
    override fun repairAll(player: Player, executor: CommandSender?, sendMessage: Boolean) {
        val inv = player.inventory
        val contents = inv.contents.toMutableList()
        contents.addAll(inv.armorContents)
        contents.addAll(inv.extraContents)
        contents.forEach { item ->
            item.repair()
        }
        if (sendMessage) {
            sendNullExecutor(player, executor, Lang.REPAIR_ALL_SUCCESS, Lang.TARGET_REPAIR_ALL_SUCCESS)
        }
    }
}