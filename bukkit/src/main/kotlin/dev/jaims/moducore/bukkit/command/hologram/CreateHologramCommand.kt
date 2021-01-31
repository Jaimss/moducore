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

import dev.jaims.hololib.core.builder.buildHologram
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun createHologramCommand(name: String, sender: Player, args: List<String>, props: CommandProperties, command: BaseCommand) {
    if (command.hologramManager.hololibManager.cachedHolograms.any { it.name.equals(name, ignoreCase = true) }) {
        command.fileManager.getMessage(Lang.HOLO_CREATE_FAIL, sender).sendMessage(sender)
        return
    }
    val hologram = buildHologram(name, sender.location) {
        val lines = args.drop(2).joinToString(" ").split("\\n").map { it.trim() }
        addPage(*lines.toTypedArray())
        showNextPage(*Bukkit.getOnlinePlayers().toTypedArray())
        command.fileManager.getMessage(Lang.HOLO_CREATE_SUCCESS, sender, mapOf("{name}" to name)).sendMessage(sender)
    }
    // command.hologramManager.saveHologram(name, hologram)
}
