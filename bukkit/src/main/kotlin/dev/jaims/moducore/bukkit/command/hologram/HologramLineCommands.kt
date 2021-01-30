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

import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.entity.Player

fun addLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        TODO(" NOT FOUND MESSAGE ")
        return
    }
    val pageIndex = hologram.getCurrentPage(sender) ?: run {
        TODO("You can't see any pages")
        return
    }
    val page = hologram.pages[pageIndex]
    val line = args.drop(2).joinToString(" ")
    page.addLines(line)
    TODO("SUCCESS")
}

fun deleteLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        TODO("hologram not found")
        return
    }
    val pageIndex = hologram.getCurrentPage(sender) ?: run {
        TODO("You can't see the page")
        return
    }
    val page = hologram.pages[pageIndex]
    val lineIndex = args.getOrNull(2)?.toInt() ?: run {
        sender.usage(command.deleteLineUsage, command.description)
        return
    }
    if (page.removeLine(lineIndex) == null) {
        TODO("no line deleted bc index is out of bounds")
    } else {
        TODO("success")
    }
}

fun insertLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        TODO("hologram not found")
        return
    }
    val pageIndex = hologram.getCurrentPage(sender) ?: run {
        TODO("you can't see the page")
        return
    }
    val page = hologram.pages[pageIndex]
    val lineIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.insertLineUsage, command.description)
        return
    }
    if (lineIndex > page.lines.size || lineIndex < 0) {
        TODO("Index out of bounds")
        return
    }
    page.insertLine(lineIndex, args.drop(3).joinToString(" "))
}

fun setLineCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        TODO("hologram not found")
        return
    }
    val pageIndex = hologram.getCurrentPage(sender) ?: run {
        TODO("You can't see the page")
        return
    }
    val page = hologram.pages[pageIndex]
    val lineIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.setLineUsage, command.description)
        return
    }
    if (lineIndex >= page.lines.size || lineIndex < 0) {
        TODO("Index out of bounds")
        return
    }
    page.setLine(lineIndex, args.drop(3).joinToString(" "))
}
