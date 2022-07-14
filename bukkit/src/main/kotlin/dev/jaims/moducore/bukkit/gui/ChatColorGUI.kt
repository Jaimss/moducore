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

package dev.jaims.moducore.bukkit.gui

import dev.jaims.mcutils.bukkit.event.waitForEvent
import dev.jaims.mcutils.bukkit.func.colorize
import dev.jaims.moducore.api.data.PlayerData
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.GUIs
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.func.langParsed
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.const.Permissions
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent

suspend fun getChatColorGUI(player: Player, plugin: ModuCore): Gui {
    val gui = Gui.gui()
        .type(GuiType.CHEST)
        .rows(5)
        .title(Component.text(plugin.api.bukkitFileManager.gui[GUIs.CHATCOLOR_TITLE].langParsed.colorize()))
        .create()

    val playerData = plugin.api.storageManager.getPlayerData(player.uniqueId)

    gui.setItem(
        2,
        2,
        WHITE.buildChatColorItem(playerData, "&f")
            .asGuiItem { handleClick(player, playerData, "&f", Permissions.CC_WHITE) })
    gui.setItem(2,
        3,
        LIGHT_GRAY.buildChatColorItem(playerData, "&7")
            .asGuiItem { handleClick(player, playerData, "&7", Permissions.CC_LIGHTGRAY) })
    gui.setItem(
        2,
        4,
        DARK_GRAY.buildChatColorItem(playerData, "&8")
            .asGuiItem { handleClick(player, playerData, "&8", Permissions.CC_GRAY) })
    gui.setItem(
        2,
        5,
        BLACK.buildChatColorItem(playerData, "&0")
            .asGuiItem { handleClick(player, playerData, "&0", Permissions.CC_BLACK) })
    gui.setItem(2,
        6,
        BROWN.buildChatColorItem(playerData, "<#964b00>")
            .asGuiItem { handleClick(player, playerData, "<#964b00>", Permissions.CC_BROWN) })
    gui.setItem(2,
        7,
        LIGHT_BLUE.buildChatColorItem(playerData, "&b")
            .asGuiItem { handleClick(player, playerData, "&b", Permissions.CC_LIGHTBLUE) })
    gui.setItem(
        2,
        8,
        AQUA.buildChatColorItem(playerData, "&3")
            .asGuiItem { handleClick(player, playerData, "&3", Permissions.CC_AQUA) })
    gui.setItem(
        3,
        2,
        DARK_BLUE.buildChatColorItem(playerData, "&1")
            .asGuiItem { handleClick(player, playerData, "&1", Permissions.CC_BLUE) })
    gui.setItem(
        3,
        3,
        PINK.buildChatColorItem(playerData, "&d")
            .asGuiItem { handleClick(player, playerData, "&d", Permissions.CC_PINK) })
    gui.setItem(3,
        4,
        MAGENTA.buildChatColorItem(playerData, "<#ff00ff>")
            .asGuiItem { handleClick(player, playerData, "<#ff00ff>", Permissions.CC_MAGENTA) })
    gui.setItem(
        3,
        5,
        PURPLE.buildChatColorItem(playerData, "&5")
            .asGuiItem { handleClick(player, playerData, "&5", Permissions.CC_PURPLE) })
    gui.setItem(
        3,
        6,
        YELLOW.buildChatColorItem(playerData, "&e")
            .asGuiItem { handleClick(player, playerData, "&e", Permissions.CC_YELLOW) })
    gui.setItem(
        3,
        7,
        ORANGE.buildChatColorItem(playerData, "&6")
            .asGuiItem { handleClick(player, playerData, "&6", Permissions.CC_ORANGE) })
    gui.setItem(
        3,
        8,
        RED.buildChatColorItem(playerData, "&4")
            .asGuiItem { handleClick(player, playerData, "&4", Permissions.CC_RED) })
    gui.setItem(
        4,
        4,
        LIME.buildChatColorItem(playerData, "&a")
            .asGuiItem { handleClick(player, playerData, "&a", Permissions.CC_LIME) })
    gui.setItem(
        4,
        6,
        GREEN.buildChatColorItem(playerData, "&2")
            .asGuiItem { handleClick(player, playerData, "&2", Permissions.CC_GREEN) })
    gui.setItem(4, 5, ItemBuilder.from(Material.OAK_SIGN)
        .name(Component.text("<#ff0000>C<#ffa500>u<#ffff00>s<#008000>t<#0000ff>o<#4b0082>m<#ee82ee>!".colorize()))
        .lore(
            Component.text("&8&l| &aLeft Click to Give a Custom Value!".colorize()),
            Component.text("&8&l| &cRight Click to clear!".colorize())
        )
        .asGuiItem {
            if (!Permissions.CC_CUSTOM.has(player)) {
                player.closeInventory()
                return@asGuiItem
            }
            if (it.isLeftClick) {
                player.closeInventory()
                player.send(Lang.CHATCOLOR_PROMPT)
                plugin.waitForEvent<AsyncPlayerChatEvent>(
                    timeoutTicks = (20 * 60).toLong(),
                    predicate = { event -> event.player.uniqueId == player.uniqueId },
                    priority = EventPriority.LOWEST
                ) { event ->
                    event.isCancelled = true
                    playerData.chatColor = event.message
                    player.send(Lang.CHATCOLOR_SUCCESS) { mes -> mes.replace("{color}", event.message.colorize()) }
                }
            } else {
                player.closeInventory()
                playerData.chatColor = null
                player.send(Lang.CHATCOLOR_REMOVED)
            }
        })

    gui.filler.fill(FILLER)
    gui.setDefaultClickAction { it.isCancelled = true }
    return gui
}

private fun ItemBuilder.buildChatColorItem(playerData: PlayerData, color: String): ItemBuilder {
    if (playerData.chatColor == color) {
        glow(true)
        lore(Component.text(REMOVE_LORE.colorize()))
    } else {
        glow(false)
        lore(Component.text(SELECT_LORE.colorize()))
    }
    return this
}

private fun handleClick(player: Player, playerData: PlayerData, color: String, permissions: Permissions) {
    if (!permissions.has(player)) {
        player.closeInventory()
        return
    }
    if (playerData.chatColor == color) {
        player.closeInventory()
        playerData.chatColor = null
        player.send(Lang.CHATCOLOR_REMOVED)
        return
    }
    playerData.chatColor = color
    player.closeInventory()
    player.send(Lang.CHATCOLOR_SUCCESS) { it.replace("{color}", (playerData.chatColor ?: "").colorize()) }
}

private const val SELECT_LORE = "&8&l| &aClick to Select!"
private const val REMOVE_LORE = "&8&l| &cClick to Remove!"

private val WHITE = ItemBuilder.from(Material.WHITE_WOOL)
    .name(Component.text("&f&lWhite".colorize()))
private val LIGHT_GRAY = ItemBuilder.from(Material.LIGHT_GRAY_WOOL)
    .name(Component.text("&7&lLight Gray".colorize()))
private val DARK_GRAY = ItemBuilder.from(Material.GRAY_WOOL)
    .name(Component.text("&8&lGray".colorize()))
private val BLACK = ItemBuilder.from(Material.BLACK_WOOL)
    .name(Component.text("&0&lBlack".colorize()))
private val BROWN = ItemBuilder.from(Material.BROWN_WOOL)
    .name(Component.text("<#964b00>&lBrown".colorize()))
private val LIGHT_BLUE = ItemBuilder.from(Material.LIGHT_BLUE_WOOL)
    .name(Component.text("&b&lLight Blue".colorize()))
private val AQUA = ItemBuilder.from(Material.CYAN_WOOL)
    .name(Component.text("&3&lAqua".colorize()))
private val DARK_BLUE = ItemBuilder.from(Material.BLUE_WOOL)
    .name(Component.text("&1&lBlue".colorize()))
private val PINK = ItemBuilder.from(Material.PINK_WOOL)
    .name(Component.text("&d&lPink".colorize()))
private val MAGENTA = ItemBuilder.from(Material.MAGENTA_WOOL)
    .name(Component.text("<#ff00ff>&lMagenta".colorize()))
private val PURPLE = ItemBuilder.from(Material.PURPLE_WOOL)
    .name(Component.text("&5&lPurple".colorize()))
private val YELLOW = ItemBuilder.from(Material.YELLOW_WOOL)
    .name(Component.text("&e&lYellow".colorize()))
private val ORANGE = ItemBuilder.from(Material.ORANGE_WOOL)
    .name(Component.text("&6&lOrange".colorize()))
private val RED = ItemBuilder.from(Material.RED_WOOL)
    .name(Component.text("&4&lRed".colorize()))
private val LIME = ItemBuilder.from(Material.LIME_WOOL)
    .name(Component.text("&a&lLime".colorize()))
private val GREEN = ItemBuilder.from(Material.GREEN_WOOL)
    .name(Component.text("&2&lGreen".colorize()))
