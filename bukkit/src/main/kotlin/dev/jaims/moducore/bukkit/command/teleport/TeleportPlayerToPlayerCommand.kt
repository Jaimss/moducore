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

package dev.jaims.moducore.bukkit.command.teleport

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.playerNotFound
import dev.jaims.moducore.bukkit.util.send
import dev.jaims.moducore.bukkit.util.usage
import io.papermc.lib.PaperLib
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TeleportPlayerToPlayerCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/tp2p <player> <target>"
    override val description: String = "Teleport a player to target."
    override val commandName: String = "teleportplayer2player"
    override val aliases: List<String> = listOf("tp2p")
    override val module: Property<Boolean> = Modules.COMMAND_TELEPORT

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument<String, String>("player", StringArgumentType.word())
                .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word())))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            2 -> {
                if (!Permissions.TELEPORT_PLAYER_TO_PLAYER.has(sender)) return
                val player = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return
                }
                PaperLib.teleportAsync(player, target.location)
                val playerName = playerManager.getName(player.uniqueId)
                val targetName = playerManager.getName(target.uniqueId)
                sender.send(Lang.TELEPORT_P2P_SUCCESS) {
                    it.replace("{player}", playerName).replace("{target}", targetName)
                }
                if (!props.isSilent) {
                    player.send(Lang.TELEPORT_P2P_PLAYER, target)
                    target.send(Lang.TELEPORT_P2P_TARGET, player)
                }
            }
            else -> sender.usage(usage, description)
        }
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val matches = mutableListOf<String>()

        when (args.size) {
            1, 2 -> matches.addAll(playerManager.getPlayerCompletions(args[0]))
        }

        return matches
    }

}