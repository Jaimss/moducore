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

import dev.jaims.moducore.api.data.Kit
import dev.jaims.moducore.api.data.give
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.GUIs
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.cooldownFormat
import dev.jaims.moducore.bukkit.func.langParsed
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.gui.FILLER
import dev.jaims.moducore.common.message.clearItalics
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.miniToComponent
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import org.bukkit.Material
import org.bukkit.entity.Player

fun getKitPreviewGUI(player: Player, plugin: ModuCore, openKit: Kit? = null): Gui {
    val items = openKit?.items?.map { GuiItem(it) } ?: listOf()

    val gui = Gui.gui()
        .type(GuiType.CHEST)
        .title(
            if (openKit == null) plugin.api.bukkitFileManager.gui[GUIs.KITPREVIEW_TITLE].langParsed
                .miniStyle().miniToComponent().clearItalics()
            else plugin.api.bukkitFileManager.gui[GUIs.KITPREVIEW_KIT_TITLE].replace(
                "{name}",
                openKit.kitInfo.displayName
            ).langParsed.miniStyle().miniToComponent().clearItalics(),
        )
        .rows(calcRows(items.size))
        .create()

    if (openKit != null) {
        // gui.rows = calcRows(items.size)
        for (item in items) {
            gui.addItem(item)
        }
        gui.setDefaultClickAction { it.isCancelled = true }
        gui.filler.fill(FILLER)
        return gui
    }

    val kits = plugin.api.kitManager.kitCache.map { kit ->
        ItemBuilder.from(Material.matchMaterial(kit.kitInfo.displayItem) ?: Material.DIRT)
            .name(kit.kitInfo.displayName.miniStyle().miniToComponent().clearItalics())
            .lore(
                *kit.kitInfo.description.langParsed.split("\n").map { it.miniStyle().miniToComponent().clearItalics() }
                    .toTypedArray(),
                plugin.api.bukkitFileManager.gui[GUIs.KITPREVIEW_LEFT].langParsed.miniStyle().miniToComponent()
                    .clearItalics(),
                plugin.api.bukkitFileManager.gui[GUIs.KITPREVIEW_RIGHT].langParsed.miniStyle().miniToComponent()
                    .clearItalics()
            )
            .glow(kit.kitInfo.glow)
            .asGuiItem {
                player.closeInventory()
                if (it.isLeftClick) {
                    getKitPreviewGUI(player, plugin, kit).open(player)
                } else if (it.isRightClick) {
                    if (!Permissions.USE_KIT.has(player) { node ->
                            node.replace(
                                "<kitname>",
                                kit.name
                            )
                        }) return@asGuiItem
                    if (!Permissions.USE_KIT_BYPASS_COOLDOWN.has(player, false) { node ->
                            node.replace(
                                "<kitname>",
                                kit.name
                            )
                        }) {
                        val timeClaimed =
                            plugin.api.storageManager.loadPlayerData(player.uniqueId).join().kitClaimTimes[kit.name]
                        if (timeClaimed != null) {
                            val timeSinceClaim = (System.currentTimeMillis() - timeClaimed) / 1000
                            if (timeSinceClaim <= kit.cooldown) {
                                player.send(Lang.KIT_COOLDOWN) { mes ->
                                    mes.replace("{name}", kit.name)
                                        .replace("{time}", (kit.cooldown - timeSinceClaim).toInt().cooldownFormat)
                                }
                                return@asGuiItem
                            }
                        }
                    }
                    kit.give(player)
                    val playerData = plugin.api.storageManager.loadPlayerData(player.uniqueId).join()
                    playerData.kitClaimTimes[kit.name] = System.currentTimeMillis()
                }
            }
    }
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