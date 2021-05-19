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
import dev.jaims.moducore.bukkit.func.playerNotFound
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.func.usage
import org.bukkit.entity.Player

/**
 * Switch the page a player is looking at or a potential target if an arg is provided for a target.
 * if [next] is true, it will go to the next page, else it will go to the previous page.
 */
fun switchPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand, next: Boolean) {
    val hologram = getHologram(sender, name, command) ?: return
    val targetName = args.getOrNull(2) ?: run {
        if (next) {
            hologram.showNextPage(sender)
        } else {
            hologram.showPreviousPage(sender)
        }
        sender.send(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender) { it.replace("{name}", name) }
        return
    }
    val target = command.playerManager.getTargetPlayer(targetName) ?: run {
        sender.playerNotFound(args[2])
        return
    }
    if (next) {
        hologram.showNextPage(target)
    } else {
        hologram.showPreviousPage(target)
    }
    sender.send(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender) { it.replace("{name}", name) }
}

/**
 * Add a page to a hologram.
 */
fun addPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val lines = args.drop(2).joinToString(" ").split("\\n").map { it.trim() }
    hologram.addPage(*lines.toTypedArray())
    sender.send(Lang.HOLO_PAGE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

/**
 * Logic for setting a page in a hologram
 */
fun setPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.setPageUsage, command.setPageDescription)
        return
    }
    if (!validatePageIndex(name, sender, hologram, pageIndex)) return
    hologram.setPage(pageIndex, *args.drop(3).joinToString(" ").split("\\n").map { it.trim() }.toTypedArray())
    sender.send(Lang.HOLO_PAGE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

/**
 * Logic for inserting a page.
 */
fun insertPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.insertPageUsage, command.insertPageDesc)
        return
    }
    if (!validatePageIndex(name, sender, hologram, pageIndex)) return
    hologram.insertPage(pageIndex, *args.drop(3).joinToString(" ").split("\\n").map { it.trim() }.toTypedArray())
    sender.send(Lang.HOLO_PAGE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

/**
 * Logic for deleting a page.
 */
fun deletePageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = getHologram(sender, name, command) ?: return
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.deletePageUsage, command.deletePageDesc)
        return
    }
    if (hologram.removePage(pageIndex) != null) {
        sender.send(Lang.HOLO_PAGE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
    } else {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
    }
}
