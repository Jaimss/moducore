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

import dev.jaims.hololib.core.Hologram
import dev.jaims.hololib.core.HologramPage
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.send
import org.bukkit.entity.Player

/**
 * @return the hologram that was searched for with the command
 */
fun getHologram(sender: Player, name: String, command: BaseCommand): Hologram? {
    return command.hologramManager.getFromCache(name) ?: run {
        sender.send(Lang.HOLO_NOT_FOUND, sender) { it.replace("{name}", name) }
        return null
    }
}

/**
 * @return the page index or null
 */
fun getPageIndex(name: String, sender: Player, hologram: Hologram): Int? {
    return hologram.getCurrentPageIndex(sender) ?: run {
        sender.send(Lang.HOLO_NOT_VIEWING_PAGE, sender) { it.replace("{name}", name) }
        return null
    }
}

/**
 * @return true if the page index is a valid index
 */
fun validatePageIndex(name: String, sender: Player, hologram: Hologram, pageIndex: Int): Boolean {
    if (pageIndex >= hologram.pages.size || pageIndex < 0) {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
        return false
    }
    return true
}

/**
 * @return true if the line index is valid for this hologram
 */
fun validateLineIndex(name: String, sender: Player, page: HologramPage, lineIndex: Int): Boolean {
    if (lineIndex >= page.lines.size || lineIndex < 0) {
        sender.send(Lang.INDEX_OUT_OF_BOUNDS, sender) { it.replace("{name}", name) }
        return false
    }
    return true
}