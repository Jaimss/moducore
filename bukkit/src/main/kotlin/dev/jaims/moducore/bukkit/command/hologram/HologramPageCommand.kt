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
import dev.jaims.moducore.bukkit.util.playerNotFound
import org.bukkit.entity.Player

fun nextPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        TODO("Hologram not found")
        return
    }
    val targetName = args.getOrNull(2) ?: run {
        hologram.showNextPage(sender)
        TODO("Success")
        return
    }
    val target = command.playerManager.getTargetPlayer(targetName) ?: run {
        sender.playerNotFound(args[2])
        return
    }
    hologram.showNextPage(target)
    TODO("Success")
}

fun previousPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        TODO("Hologram not found")
        return
    }
    val targetName = args.getOrNull(2) ?: run {
        hologram.showPreviousPage(sender)
        TODO("Success")
        return
    }
    val target = command.playerManager.getTargetPlayer(targetName) ?: run {
        sender.playerNotFound(args[2])
        return
    }
    hologram.showPreviousPage(target)
    TODO("Success")
}

fun addPageCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: HologramCommand) {
    val hologram = command.hologramManager.getHologram(name) ?: run {
        TODO("Hologram not found")
        return
    }
    val lines = args.drop(2).joinToString(" ").split("\\n").map { it.trim() }
    hologram.addPage(*lines.toTypedArray())
    TODO("success message")
}