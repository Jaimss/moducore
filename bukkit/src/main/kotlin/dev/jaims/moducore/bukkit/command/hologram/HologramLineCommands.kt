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

@file:Suppress("UNUSED_PARAMETER")

package dev.jaims.moducore.bukkit.command.hologram

import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.send
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.entity.Player

/**
 * Logic for adding a line to a hologram.
 */
fun addLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val pageIndex = getPageIndex(name, sender, hologram) ?: return
    val page = hologram.pages[pageIndex]
    val line = args.drop(2).joinToString(" ")
    page.addLines(line)
    sender.send(Lang.HOLO_LINE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

/**
 * Logic for deleting a line from a page.
 */
fun deleteLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val pageIndex = getPageIndex(name, sender, hologram) ?: return
    val page = hologram.pages[pageIndex]
    val lineIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.deleteLineUsage, command.description)
        return
    }
    if (page.removeLine(lineIndex) == null) {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
    } else {
        sender.send(Lang.HOLO_LINE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
    }
}

/**
 * Logic for inserting a line
 */
fun insertLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val pageIndex = getPageIndex(name, sender, hologram) ?: return
    val page = hologram.pages[pageIndex]
    val lineIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.insertLineUsage, command.description)
        return
    }
    if (!validateLineIndex(name, sender, page, lineIndex)) return
    page.insertLine(lineIndex, args.drop(3).joinToString(" "))
    sender.send(Lang.HOLO_LINE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

/**
 * Logic for setting a line
 */
fun setLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val pageIndex = getPageIndex(name, sender, hologram) ?: return
    val page = hologram.pages[pageIndex]
    val lineIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.setLineUsage, command.description)
        return
    }
    if (!validateLineIndex(name, sender, page, lineIndex)) return
    page.setLine(lineIndex, args.drop(3).joinToString(" "))
    sender.send(Lang.HOLO_LINE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}
