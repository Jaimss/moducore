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
import dev.jaims.moducore.bukkit.util.playerNotFound
import dev.jaims.moducore.bukkit.util.send
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.entity.Player

fun nextPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return
    }
    val targetName = args.getOrNull(2) ?: run {
        hologram.showNextPage(sender)
        sender.send(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender) { it.replace("{name}", name) }
        return
    }
    val target = command.playerManager.getTargetPlayer(targetName) ?: run {
        sender.playerNotFound(args[2])
        return
    }
    hologram.showNextPage(target)
    sender.send(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender) { it.replace("{name}", name) }
}

fun previousPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return
    }
    val targetName = args.getOrNull(2) ?: run {
        hologram.showPreviousPage(sender)
        sender.send(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender) { it.replace("{name}", name) }
        return
    }
    val target = command.playerManager.getTargetPlayer(targetName) ?: run {
        sender.playerNotFound(args[2])
        return
    }
    hologram.showPreviousPage(target)
    sender.send(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender) { it.replace("{name}", name) }
}

fun addPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return
    }
    val lines = args.drop(2).joinToString(" ").split("\\n").map { it.trim() }
    hologram.addPage(*lines.toTypedArray())
    sender.send(Lang.HOLO_PAGE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

fun setPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return
    }
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.setPageUsage, command.setPageDescription)
        return
    }
    if (pageIndex >= hologram.pages.size || pageIndex < 0) {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
        return
    }
    hologram.setPage(pageIndex, *args.drop(3).joinToString(" ").split("\\n").map { it.trim() }.toTypedArray())
    sender.send(Lang.HOLO_PAGE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

fun insertPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return
    }
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.insertPageUsage, command.insertPageDesc)
        return
    }
    if (pageIndex > hologram.pages.size || pageIndex < 0) {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
        return
    }
    hologram.insertPage(pageIndex, *args.drop(3).joinToString(" ").split("\\n").map { it.trim() }.toTypedArray())
    sender.send(Lang.HOLO_PAGE_MOD_SUCCESS, sender) { it.replace("{name}", name) }
}

fun deletePageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return
    }
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
