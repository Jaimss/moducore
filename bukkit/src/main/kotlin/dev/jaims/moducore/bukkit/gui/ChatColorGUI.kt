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
import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.moducore.api.data.PlayerData
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.GUIs
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.langParsed
import dev.jaims.moducore.bukkit.util.send
import me.mattstudios.mfgui.gui.components.GuiType
import me.mattstudios.mfgui.gui.components.ItemBuilder
import me.mattstudios.mfgui.gui.guis.Gui
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent

suspend fun getChatColorGUI(player: Player, plugin: ModuCore): Gui {
    val gui = Gui(GuiType.CHEST, plugin.api.fileManager.gui[GUIs.CHATCOLOR_TITLE].langParsed.colorize())

    val playerData = plugin.api.storageManager.getPlayerData(player.uniqueId)

    gui.rows = 5

    gui.setItem(2, 2, WHITE.buildChatColorItem(playerData, "&f").asGuiItem { handleClick(player, playerData, "&f", Permissions.CC_WHITE) })
    gui.setItem(2,
        3,
        LIGHT_GRAY.buildChatColorItem(playerData, "&7").asGuiItem { handleClick(player, playerData, "&7", Permissions.CC_LIGHTGRAY) })
    gui.setItem(2, 4, DARK_GRAY.buildChatColorItem(playerData, "&8").asGuiItem { handleClick(player, playerData, "&8", Permissions.CC_GRAY) })
    gui.setItem(2, 5, BLACK.buildChatColorItem(playerData, "&0").asGuiItem { handleClick(player, playerData, "&0", Permissions.CC_BLACK) })
    gui.setItem(2,
        6,
        BROWN.buildChatColorItem(playerData, "<#964b00>").asGuiItem { handleClick(player, playerData, "<#964b00>", Permissions.CC_BROWN) })
    gui.setItem(2,
        7,
        LIGHT_BLUE.buildChatColorItem(playerData, "&b").asGuiItem { handleClick(player, playerData, "&b", Permissions.CC_LIGHTBLUE) })
    gui.setItem(2, 8, AQUA.buildChatColorItem(playerData, "&3").asGuiItem { handleClick(player, playerData, "&3", Permissions.CC_AQUA) })
    gui.setItem(3, 2, DARK_BLUE.buildChatColorItem(playerData, "&1").asGuiItem { handleClick(player, playerData, "&1", Permissions.CC_BLUE) })
    gui.setItem(3, 3, PINK.buildChatColorItem(playerData, "&d").asGuiItem { handleClick(player, playerData, "&d", Permissions.CC_PINK) })
    gui.setItem(3,
        4,
        MAGENTA.buildChatColorItem(playerData, "<#ff00ff>").asGuiItem { handleClick(player, playerData, "<#ff00ff>", Permissions.CC_MAGENTA) })
    gui.setItem(3, 5, PURPLE.buildChatColorItem(playerData, "&5").asGuiItem { handleClick(player, playerData, "&5", Permissions.CC_PURPLE) })
    gui.setItem(3, 6, YELLOW.buildChatColorItem(playerData, "&e").asGuiItem { handleClick(player, playerData, "&e", Permissions.CC_YELLOW) })
    gui.setItem(3, 7, ORANGE.buildChatColorItem(playerData, "&6").asGuiItem { handleClick(player, playerData, "&6", Permissions.CC_ORANGE) })
    gui.setItem(3, 8, RED.buildChatColorItem(playerData, "&4").asGuiItem { handleClick(player, playerData, "&4", Permissions.CC_RED) })
    gui.setItem(4, 4, LIME.buildChatColorItem(playerData, "&a").asGuiItem { handleClick(player, playerData, "&a", Permissions.CC_LIME) })
    gui.setItem(4, 6, GREEN.buildChatColorItem(playerData, "&2").asGuiItem { handleClick(player, playerData, "&2", Permissions.CC_GREEN) })
    gui.setItem(4, 5, ItemBuilder.from(Material.OAK_SIGN)
        .setName("<#ff0000>C<#ffa500>u<#ffff00>s<#008000>t<#0000ff>o<#4b0082>m<#ee82ee>!".colorize())
        .setLore("&8&l| &aLeft Click to Give a Custom Value!".colorize(), "&8&l| &cRight Click to clear!".colorize())
        .asGuiItem {
            if (!Permissions.CC_CUSTOM.has(player)) {
                player.closeInventory()
                return@asGuiItem
            }
            if (it.isLeftClick) {
                player.closeInventory()
                player.send(Lang.CHATCOLOR_PROMPT)
                plugin.waitForEvent<AsyncPlayerChatEvent>(
                    timeoutTicks = 20 * 60,
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
        setLore(REMOVE_LORE.colorize())
    } else {
        glow(false)
        setLore(SELECT_LORE.colorize())
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
    .setName("&f&lWhite".colorize())
private val LIGHT_GRAY = ItemBuilder.from(Material.LIGHT_GRAY_WOOL)
    .setName("&7&lLight Gray".colorize())
private val DARK_GRAY = ItemBuilder.from(Material.GRAY_WOOL)
    .setName("&8&lGray".colorize())
private val BLACK = ItemBuilder.from(Material.BLACK_WOOL)
    .setName("&0&lBlack".colorize())
private val BROWN = ItemBuilder.from(Material.BROWN_WOOL)
    .setName("<#964b00>&lBrown".colorize())
private val LIGHT_BLUE = ItemBuilder.from(Material.LIGHT_BLUE_WOOL)
    .setName("&b&lLight Blue".colorize())
private val AQUA = ItemBuilder.from(Material.CYAN_WOOL)
    .setName("&3&lAqua".colorize())
private val DARK_BLUE = ItemBuilder.from(Material.BLUE_WOOL)
    .setName("&1&lBlue".colorize())
private val PINK = ItemBuilder.from(Material.PINK_WOOL)
    .setName("&d&lPink".colorize())
private val MAGENTA = ItemBuilder.from(Material.MAGENTA_WOOL)
    .setName("<#ff00ff>&lMagenta".colorize())
private val PURPLE = ItemBuilder.from(Material.PURPLE_WOOL)
    .setName("&5&lPurple".colorize())
private val YELLOW = ItemBuilder.from(Material.YELLOW_WOOL)
    .setName("&e&lYellow".colorize())
private val ORANGE = ItemBuilder.from(Material.ORANGE_WOOL)
    .setName("&6&lOrange".colorize())
private val RED = ItemBuilder.from(Material.RED_WOOL)
    .setName("&4&lRed".colorize())
private val LIME = ItemBuilder.from(Material.LIME_WOOL)
    .setName("&a&lLime".colorize())
private val GREEN = ItemBuilder.from(Material.GREEN_WOOL)
    .setName("&2&lGreen".colorize())
