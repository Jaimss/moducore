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
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.api.error.Errors
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.playerNotFound
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.common.func.decimalFormat
import io.papermc.lib.PaperLib
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class RandomTeleportCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/randomteleport [target]"
    override val description: String = "Teleport to a random location on the map."
    override val commandName: String = "randomteleport"
    override val aliases: List<String> = listOf("rtp", "wild")
    override val module: Property<Boolean> = Modules.COMMAND_RANDOM_TELEPORT

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(
                RequiredArgumentBuilder.argument<String, String>("target", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("world", StringArgumentType.word()))
            )

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            0 -> {
                if (!Permissions.TELEPORT_RANDOM.has(sender)) return
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }
                val loc = getLocation(sender, null)

                PaperLib.teleportAsync(sender, loc)
                sendPlayerMessage(sender, loc)
            }
            1, 2 -> {
                if (!Permissions.TELEPORT_RANDOM_OTHERS.has(sender)) return

                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }

                val world = args.getOrNull(1) ?: target.world.name
                val loc = getLocation(target, Bukkit.getWorld(world))

                PaperLib.teleportAsync(target, loc)
                if (!props.isSilent) {
                    sendPlayerMessage(target, loc)
                }
                sender.send(Lang.TELEPORT_POSITION_TARGET, target) {
                    it.replace("{x}", decimalFormat.format(loc.x)).replace("{y}", decimalFormat.format(loc.y))
                        .replace("{z}", decimalFormat.format(loc.z))
                        .replace("{world}", loc.world?.name ?: target.world.name)
                }
            }
        }
    }

    private fun sendPlayerMessage(player: Player, loc: Location) {
        player.send(Lang.TELEPORT_POSITION_SUCCESS, player) {
            it.replace("{x}", decimalFormat.format(loc.x)).replace("{y}", decimalFormat.format(loc.y))
                .replace("{z}", decimalFormat.format(loc.z))
                .replace("{world}", loc.world?.name ?: player.world.name)
        }
    }

    private fun getLocation(player: Player, providedWorld: World?): Location {
        val x = Random.nextDouble(-fileManager.config[Config.RTP_MAX_X], fileManager.config[Config.RTP_MAX_X])
        val z = Random.nextDouble(-fileManager.config[Config.RTP_MAX_Z], fileManager.config[Config.RTP_MAX_Z])
        val defaultWorldName = fileManager.config[Config.RTP_DEFAULT_WORLD]

        val world = providedWorld ?: if (defaultWorldName == "") player.world else Bukkit.getWorld(defaultWorldName)
            ?: run {
                Errors.INVALID_RTP_WORLD.log()
                player.world
            }

        val block = world.getHighestBlockAt(x.toInt(), z.toInt())
        if (block.type == Material.WATER || block.type == Material.LAVA) {
            return getLocation(player, world)
        }
        return block.location.add(0.5, 1.1, 0.5)
    }

}