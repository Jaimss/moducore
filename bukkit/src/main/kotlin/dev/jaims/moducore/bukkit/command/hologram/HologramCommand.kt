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

package dev.jaims.moducore.bukkit.command.hologram

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.send
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Suppress("MemberVisibilityCanBePrivate")
class HologramCommand(override val plugin: ModuCore) : BaseCommand {
    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        // perms check
        if (!Permissions.HOLOGRAM.has(sender)) return
        // only players
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        // help || list
        when (args.getOrNull(0)?.toLowerCase()) {
            "help" -> {
                with(sender) {
                    send("&3&lHolograms Help")
                    send("&bAll indexes are 0-indexed, and you can split into multiple lines with \\n.")
                    usage(createUsage, createDesc, false)
                    usage(deleteUsage, deleteDesc, false)
                    usage(addLineUsage, addLineDesc, false)
                    usage(setLineUsage, setLineDesc, false)
                    usage(insertLineUsage, insertLineDesc, false)
                    usage(deleteLineUsage, deleteLineDesc, false)
                    usage(addPageUsage, addPageDesc, false)
                    usage(setPageUsage, setPageDescription, false)
                    usage(insertPageUsage, insertPageDesc, false)
                    usage(deletePageUsage, deletePageDesc, false)
                    usage(nextPageUsage, nextPageDesc, false)
                    usage(previousPageUsage, previousPageDesc, false)
                    usage("/holo info <name>", "Get information about the hologram", false)
                    usage("/holo tphere <name>", "Teleport a hologram to you", false)
                }
                return
            }
            "list" -> {
                sender.send("&3${hologramManager.hololibManager.cachedHolograms.joinToString(", ") { it.name }}")
                return
            }
        }
        // every time you need a name
        val name = args.getOrNull(1) ?: run {
            sender.usage(usage, description)
            return
        }
        when (args.getOrNull(0)?.toLowerCase()) {
            "create" -> createHologramCommand(name, sender, args, props, this)
            "delete" -> deleteHologramCommand(name, sender, args, props, this)
            "addline" -> addLineCommand(name, sender, args, props, this)
            "setline" -> setLineCommand(name, sender, args, props, this)
            "insertline" -> insertLineCommand(name, sender, args, props, this)
            "deleteline" -> deleteLineCommand(name, sender, args, props, this)
            "addpage" -> addPageCommand(name, sender, args, props, this)
            "setpage" -> setPageCommand(name, sender, args, props, this)
            "insertpage" -> insertPageCommand(name, sender, args, props, this)
            "deletepage" -> deletePageCommand(name, sender, args, props, this)
            "nextpage" -> nextPageCommand(name, sender, args, props, this)
            "previouspage" -> previousPageCommand(name, sender, args, props, this)
            "movehere", "tphere" -> moveHereCommand(name, sender, args, props, this)
            "info" -> {
                val hologram = hologramManager.getFromCache(name) ?: run {
                    sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
                    return
                }
                sender.send(Lang.HOLOGRAM_INFO_HEADER, sender) { it.replace("{name}", name).replace("{pages}", hologram.pages.size.toString()) }
                hologram.pages.forEachIndexed { index, page ->
                    sender.send(Lang.HOLOGRAM_PAGE_FORMAT, sender) {
                        it.replace("{index}", index.toString()).replace("{name}", name).replace("{lines}", page.lines.size.toString())
                    }
                    page.lines.forEachIndexed { i, line ->
                        sender.send(Lang.HOLOGRAM_INFO_LINES_FORMAT, sender) {
                            it.replace("{name}", name).replace("{index}", i.toString()).replace("{line}", line.content)
                        }
                    }
                }
            }
            else -> sender.usage(usage, description)
        }
    }

    override val usage: String =
        "/holo help"
    override val description: String =
        "Manage the holograms on your server!"
    override val commandName: String = "hologram"
    override val aliases: List<String> = listOf("holo")

    // create
    val createUsage = "/holo create <name> [lines]"
    val createDesc = "Create a hologram with some lines on the first page."

    // delete
    val deleteUsage = "/holo delete <name>"
    val deleteDesc = "Delete a hologram."

    // addline
    val addLineUsage = "/holo addline <name> <line content>"
    val addLineDesc = "Add a line to a hologam."

    // setline
    val setLineUsage = "/holo setline <name> <line number> <line content>"
    val setLineDesc = "Set a specific line of a hologram."

    // insertline
    val insertLineUsage = "/holo insertline <name> <line number> <line content>"
    val insertLineDesc = "Insert a line at a given position. All following lines will be moved down."

    // deleteline
    val deleteLineUsage = "/holo deleteline <name> <line number>"
    val deleteLineDesc = "Delete a line from a hologram."

    // addpage
    val addPageUsage = "/holo addpage <name> [lines]"
    val addPageDesc = "Add a page on to the end of this hologram."

    // set page
    val setPageUsage = "/holo setpage <name> <page number> [lines]"
    val setPageDescription = "Set a page at a given index to a set of lines."

    // insert a new page
    val insertPageUsage = "/holo insertpage <name> <page number> [lines]"
    val insertPageDesc = "Insert a page into a position, moving all other pages back after it."

    // delete page
    val deletePageUsage = "/holo deletepage <name> <page number> [lines]"
    val deletePageDesc = "Delete a page from a position"

    // nextpage
    val nextPageUsage = "/holo nextpage <name> [target]"
    val nextPageDesc = "Manually set yourself or someone else to the next page."

    // previous page
    val previousPageUsage = "/holo previoupsage <name> [target]"
    val previousPageDesc = "Manually set yourself or someone else to the previous page."


    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(LiteralArgumentBuilder.literal<String>("tphere")
                .then(RequiredArgumentBuilder.argument("hologram name", StringArgumentType.word())))
            .then(LiteralArgumentBuilder.literal<String>("movehere")
                .then(RequiredArgumentBuilder.argument("hologram name", StringArgumentType.word())))

            .then(LiteralArgumentBuilder.literal<String>("info")
                .then(RequiredArgumentBuilder.argument("hologram name", StringArgumentType.word())))

            .then(LiteralArgumentBuilder.literal("help"))

            .then(LiteralArgumentBuilder.literal("list"))

            .then(LiteralArgumentBuilder.literal<String>("create")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("lines", StringArgumentType.greedyString()))))

            .then(LiteralArgumentBuilder.literal<String>("delete")
                .then(RequiredArgumentBuilder.argument("hologram name", StringArgumentType.word())))

            .then(LiteralArgumentBuilder.literal<String>("addline")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("line", StringArgumentType.greedyString()))))

            .then(LiteralArgumentBuilder.literal<String>("setline")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument<String, Int>("line number", IntegerArgumentType.integer(0))
                        .then(RequiredArgumentBuilder.argument("line content", StringArgumentType.greedyString())))))

            .then(LiteralArgumentBuilder.literal<String>("insertline")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument<String, Int>("line number", IntegerArgumentType.integer(0))
                        .then(RequiredArgumentBuilder.argument("line content", StringArgumentType.greedyString())))))

            .then(LiteralArgumentBuilder.literal<String>("deleteline")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("line number", IntegerArgumentType.integer(0)))))

            .then(LiteralArgumentBuilder.literal<String>("addpage")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("lines", StringArgumentType.greedyString()))))

            .then(LiteralArgumentBuilder.literal<String>("setpage")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument<String, Int>("page number", IntegerArgumentType.integer(0))
                        .then(RequiredArgumentBuilder.argument("page content", StringArgumentType.greedyString())))))

            .then(LiteralArgumentBuilder.literal<String>("insertpage")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument<String, Int>("page number", IntegerArgumentType.integer(0))
                        .then(RequiredArgumentBuilder.argument("page content", StringArgumentType.greedyString())))))

            .then(LiteralArgumentBuilder.literal<String>("deletepage")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("page number", IntegerArgumentType.integer(0)))))

            .then(LiteralArgumentBuilder.literal<String>("nextpage")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word()))))

            .then(LiteralArgumentBuilder.literal<String>("previouspage")
                .then(RequiredArgumentBuilder.argument<String, String>("hologram name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word()))))
}