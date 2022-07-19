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

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.bukkit.perm.Permissions
import io.papermc.lib.PaperLib
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportPositionCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/tppos <x> <y> <z> [world]"
    override val description: String = "Teleport to a set of coordinates."
    override val commandName: String = "teleportposition"
    override val aliases: List<String> = listOf("tppos")
    override val module: Property<Boolean> = Modules.COMMAND_TELEPORT

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument<String, Int>("x", IntegerArgumentType.integer())
                .then(RequiredArgumentBuilder.argument<String, Int>("y", IntegerArgumentType.integer())
                    .then(RequiredArgumentBuilder.argument<String, Int>("z", IntegerArgumentType.integer())
                        .then(RequiredArgumentBuilder.argument("world", StringArgumentType.word())))))

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            3, 4 -> {
                if (!Permissions.TELEPORT_POS.has(sender)) return
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }
                val x = args[0].toDoubleOrNull() ?: kotlin.run { sender.invalidNumber(); return }
                val y = args[1].toDoubleOrNull() ?: kotlin.run { sender.invalidNumber(); return }
                val z = args[2].toDoubleOrNull() ?: kotlin.run { sender.invalidNumber(); return }
                var world = sender.location.world
                val worldName = args.getOrNull(3)
                if (worldName != null) {
                    world = Bukkit.getWorld(worldName) ?: sender.location.world
                }
                PaperLib.teleportAsync(sender, Location(world, x, y, z))
                sender.send(Lang.TELEPORT_POSITION_SUCCESS) {
                    it.replace("{x}", decimalFormat.format(x)).replace("{y}", decimalFormat.format(y)).replace("{z}", decimalFormat.format(z))
                        .replace("{world}", world?.name ?: sender.world.name)
                }
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val matches = mutableListOf<String>()

        when (args.size) {
            1 -> matches.addAll((1..100).map(Int::toString).filter { it.contains(args[0], ignoreCase = true) })
            2 -> matches.addAll((1..100).map(Int::toString).filter { it.contains(args[1], ignoreCase = true) })
            3 -> matches.addAll((1..100).map(Int::toString).filter { it.contains(args[2], ignoreCase = true) })
            4 -> matches.addAll(Bukkit.getServer().worlds.map(World::getName).filter { it.contains(args[3], ignoreCase = true) })
        }

        return matches
    }

}