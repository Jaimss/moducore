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

package dev.jaims.moducore.bukkit.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Perm
import dev.jaims.moducore.bukkit.util.invalidNumber
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HologramCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/hologram <create|delete|setline> <name> [page] [line index] [line]"
    override val description: String = "Manage Server Holograms. The page and line arguments only apply for the setline command."
    override val commandName: String = "hologram"

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Perm.HOLOGRAM.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        val name = args.getOrNull(1) ?: run {
            sender.usage(usage, description)
            return
        }
        var hologram = hologramManager.getHologramFromFile(name)
        when (args.getOrNull(0)?.toLowerCase()) {
            "create" -> {
                hologram = hologramManager.createHologram(name, sender.location, listOf())
                hologramManager.showToPlayer(hologram.pages.firstOrNull(), *Bukkit.getOnlinePlayers().toTypedArray())
                // TODO
                sender.send("Created")
            }
            "delete" -> {
                hologramManager.deleteHologram(name)
                // TODO
                sender.send("Deleted")
            }
            "setline" -> {
                // get the page, line index and line
                val page = args.getOrNull(2)?.toIntOrNull()?.minus(1) ?: run {
                    sender.invalidNumber()
                    sender.usage(usage, description)
                    return
                }
                val lineIndex = args.getOrNull(3)?.toIntOrNull()?.minus(1) ?: run {
                    sender.invalidNumber()
                    sender.usage(usage, description)
                    return
                }
                val line = args.drop(4).joinToString(" ")
                // add the line
                if (hologram == null) {
                    hologramNotFound(sender, name)
                    return
                }
                hologramManager.addLine(name, hologram, page, lineIndex, line)
                // TODO
                sender.send("Setline!")
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                2 -> addAll(hologramManager.getAllHolograms().map { it.key }.filter { it.startsWith(args[0]) })
            }
        }
    }

    private fun hologramNotFound(sender: Player, name: String) {
        fileManager.getMessage(Lang.HOLO_NOT_FOUND, sender as? Player, mapOf("{hologram}" to name)).sendMessage(sender)
    }

    override val commodoreSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(
                LiteralArgumentBuilder.literal<String>("create")
                    .then(RequiredArgumentBuilder.argument("hologram name", StringArgumentType.word()))
            )
            .then(
                LiteralArgumentBuilder.literal<String>("delete")
                    .then(RequiredArgumentBuilder.argument("hologram name", StringArgumentType.word()))
            )
            .then(
                LiteralArgumentBuilder.literal<String>("setline")
                    .then(
                        RequiredArgumentBuilder.argument<String, String>("hologramname", StringArgumentType.word())
                            .then(
                                RequiredArgumentBuilder.argument<String, Int>("page", IntegerArgumentType.integer())
                                    .then(
                                        RequiredArgumentBuilder.argument<String, Int>("lineindex", IntegerArgumentType.integer())
                                            .then(RequiredArgumentBuilder.argument("line", StringArgumentType.greedyString()))
                                    )
                            )
                    )
            )

}