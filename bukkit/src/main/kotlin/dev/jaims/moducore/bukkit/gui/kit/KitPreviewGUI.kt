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

package dev.jaims.moducore.bukkit.gui.kit

import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.moducore.api.data.Kit
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.GUIs
import dev.jaims.moducore.bukkit.gui.FILLER
import dev.jaims.moducore.bukkit.util.langParsed
import me.mattstudios.mfgui.gui.components.GuiType
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.guis.Gui
import me.mattstudios.mfgui.gui.guis.GuiItem
import org.bukkit.Material
import org.bukkit.entity.Player

fun getKitPreviewGUI(player: Player, plugin: ModuCore, openKit: Kit? = null): Gui {
    val gui = Gui(GuiType.CHEST, (
            if (openKit == null) plugin.api.fileManager.gui[GUIs.KITPREVIEW_TITLE]
            else plugin.api.fileManager.gui[GUIs.KITPREVIEW_KIT_TITLE].replace("{name}", openKit.kitInfo.displayName)).langParsed.colorize()
    )

    if (openKit != null) {
        val items = openKit.items.map { GuiItem(it) }
        gui.rows = calcRows(items.size)
        for (item in items) {
            gui.addItem(item)
        }
        gui.setDefaultClickAction { it.isCancelled = true }
        gui.filler.fill(FILLER)
        return gui
    }

    val kits = plugin.api.kitManager.kitCache.map { kit ->
        ItemBuilder.from(Material.matchMaterial(kit.kitInfo.displayItem) ?: Material.DIRT)
            .setName(kit.kitInfo.displayName.colorize())
            .setLore(*kit.kitInfo.description.langParsed.split("\n").colorize().toTypedArray(),
                plugin.api.fileManager.gui[GUIs.KITPREVIEW_LEFT].langParsed.colorize(),
                plugin.api.fileManager.gui[GUIs.KITPREVIEW_RIGHT].langParsed.colorize())
            .glow(kit.kitInfo.glow)
            .asGuiItem {
                player.closeInventory()
                if (it.isLeftClick) {
                    getKitPreviewGUI(player, plugin, kit).open(player)
                } else if (it.isRightClick) {
                    kit.give(player)
                }
            }
    }
    gui.rows = calcRows(kits.size)
    for (kit in kits) {
        gui.addItem(kit)
    }
    gui.filler.fill(FILLER)
    gui.setDefaultClickAction { it.isCancelled = true }
    return gui
}

private fun calcRows(items: Int): Int {
    return when {
        items <= 9 -> 1
        items <= 9 * 2 -> 2
        items <= 9 * 3 -> 3
        items <= 9 * 4 -> 4
        items <= 9 * 5 -> 5
        else -> 6
    }
}