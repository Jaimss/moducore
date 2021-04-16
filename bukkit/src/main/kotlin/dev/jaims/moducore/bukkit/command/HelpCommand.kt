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

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.bukkitMessage
import dev.jaims.moducore.bukkit.util.langParsed
import dev.jaims.moducore.bukkit.util.send
import me.mattstudios.config.properties.Property
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HelpCommand(override val plugin: ModuCore) : BaseCommand {

    override val usage: String = "/help [command] [-p <page>]"
    override val description: String = "Show help menus for all commands or a specific one. You can set a page using -p number."
    override val commandName: String = "help"
    override val module: Property<Boolean> = Modules.COMMAND_HELP

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument("command", StringArgumentType.greedyString()))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        // get a list of commands to include
        var filter = args.getOrNull(0) ?: ""
        if (filter == "-p") filter = ""
        val matches = allCommands.filter { it.commandName.contains(filter, ignoreCase = true) }.sortedBy { it.commandName }

        if (matches.isEmpty()) {
            sender.send(Lang.HELP_NOT_FOUND) { it.replace("{name}", filter) }
            return
        }

        if (sender !is Player) {
            sender.send(
                mutableListOf<String>().apply {
                    matches.forEach {
                        add(fileManager.lang[Lang.HELP_COMMAND_USAGE].langParsed.replace("{usage}", it.usage)
                            .replace("{description}", it.description))
                    }
                }
            )
            return
        }

        val chunked = matches.chunked(7)
        val pages = chunked.mapIndexed { index, commands ->
            // get the page or a default
            // TODO replace with buildList when its no longer experimental
            val lines = mutableListOf<String>().apply {
                commands.forEach {
                    add(fileManager.lang[Lang.HELP_COMMAND_USAGE].langParsed.replace("{usage}", it.usage)
                        .replace("{description}", it.description))
                }
            }.toList()
            // add the new page
            Page(lines, index + 1, chunked.size, filter)
        }

        val pageIndex = args.indexOf("-p") + 1
        val page = args.getOrNull(pageIndex)?.toIntOrNull() ?: 1

        if (page <= 0 || page > pages.size) {
            sender.send(Lang.HELP_INVALID_PAGE)
            return
        }

        pages[page - 1].send(sender)
    }

    fun Page.send(sender: Player) {
        sender.send(
            fileManager.lang[Lang.HELP_HEADER].langParsed
                .replace("{filter}", if (filter == "") "none" else filter)
        )
        lines.forEach { bukkitMessage.parse(it).sendMessage(sender) }
        sender.spigot().sendMessage(sender.uniqueId, *ComponentBuilder().apply {
            // back
            append("««")
            bold(true)
            if (current >= 2) color(ChatColor.AQUA) else color(ChatColor.GRAY)
            if (current >= 2) event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help $filter -p ${current - 1}"))

            // page number
            append(" $current / $total ")
            color(ChatColor.GOLD)

            // forward
            append("»»")
            bold(true)
            if (current <= (total - 1)) color(ChatColor.AQUA) else color(ChatColor.GRAY)
            if (current <= (total - 1)) event(ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help $filter -p ${current + 1}"))
        }.create())
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(allCommands.map { it.commandName }.filter { it.startsWith(args[0], ignoreCase = true) })
            }
        }

    }
}

data class Page(
    val lines: List<String>,
    val current: Int,
    val total: Int,
    val filter: String
)