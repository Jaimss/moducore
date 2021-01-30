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
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.entity.Player

fun nextPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        command.fileManager.getMessage(Lang.HOLO_NOT_FOUND, sender, mapOf("{name}" to name)).sendMessage(sender)
        return
    }
    val targetName = args.getOrNull(2) ?: run {
        hologram.showNextPage(sender)
        command.fileManager.getMessage(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender).sendMessage(sender)
        return
    }
    val target = command.playerManager.getTargetPlayer(targetName) ?: run {
        sender.playerNotFound(args[2])
        return
    }
    hologram.showNextPage(target)
    command.fileManager.getMessage(Lang.HOLO_PAGE_SWITCH_SUCCESS, target).sendMessage(sender)
}

fun previousPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        command.fileManager.getMessage(Lang.HOLO_NOT_FOUND, sender, mapOf("{name}" to name)).sendMessage(sender)
        return
    }
    val targetName = args.getOrNull(2) ?: run {
        hologram.showPreviousPage(sender)
        command.fileManager.getMessage(Lang.HOLO_PAGE_SWITCH_SUCCESS, sender).sendMessage(sender)
        return
    }
    val target = command.playerManager.getTargetPlayer(targetName) ?: run {
        sender.playerNotFound(args[2])
        return
    }
    hologram.showPreviousPage(target)
    command.fileManager.getMessage(Lang.HOLO_PAGE_SWITCH_SUCCESS, target).sendMessage(sender)
}

fun addPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        command.fileManager.getMessage(Lang.HOLO_NOT_FOUND, sender, mapOf("{name}" to name)).sendMessage(sender)
        return
    }
    val lines = args.drop(2).joinToString(" ").split("\\n").map { it.trim() }
    hologram.addPage(*lines.toTypedArray())
    command.hologramManager.saveHologram(name, hologram)
    command.fileManager.getMessage(Lang.HOLO_LINE_MOD_SUCCESS, sender).sendMessage(sender)
}

fun setPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        command.fileManager.getMessage(Lang.HOLO_NOT_FOUND, sender, mapOf("{name}" to name)).sendMessage(sender)
        return
    }
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.setPageUsage, command.setPageDescription)
        return
    }
    if (pageIndex >= hologram.pages.size || pageIndex < 0) {
        command.fileManager.getMessage(Lang.INDEX_OUT_OF_BOUNDS, sender).sendMessage(sender)
        return
    }
    hologram.setPage(pageIndex, *args.drop(3).joinToString(" ").split("\\n").map { it.trim() }.toTypedArray())
    command.hologramManager.saveHologram(name, hologram)
    command.fileManager.getMessage(Lang.HOLO_LINE_MOD_SUCCESS, sender).sendMessage(sender)
}

fun insertPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        command.fileManager.getMessage(Lang.HOLO_NOT_FOUND, sender, mapOf("{name}" to name)).sendMessage(sender)
        return
    }
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.insertPageUsage, command.insertPageDesc)
        return
    }
    if (pageIndex > hologram.pages.size || pageIndex < 0) {
        command.fileManager.getMessage(Lang.INDEX_OUT_OF_BOUNDS, sender).sendMessage(sender)
        return
    }
    hologram.insertPage(pageIndex, *args.drop(3).joinToString(" ").split("\\n").map { it.trim() }.toTypedArray())
    command.hologramManager.saveHologram(name, hologram)
    command.fileManager.getMessage(Lang.HOLO_LINE_MOD_SUCCESS, sender).sendMessage(sender)
}

fun deletePageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        command.fileManager.getMessage(Lang.HOLO_NOT_FOUND, sender, mapOf("{name}" to name)).sendMessage(sender)
        return
    }
    val pageIndex = args.getOrNull(2)?.toIntOrNull() ?: run {
        sender.usage(command.deletePageUsage, command.deletePageDesc)
        return
    }
    if (hologram.removePage(pageIndex) != null) {
        command.fileManager.getMessage(Lang.HOLO_LINE_MOD_SUCCESS, sender).sendMessage(sender)
    } else {
        command.fileManager.getMessage(Lang.INDEX_OUT_OF_BOUNDS, sender).sendMessage(sender)
    }
    command.hologramManager.saveHologram(name, hologram)
}
