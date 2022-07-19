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

import dev.jaims.moducore.api.data.PlayerData
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.GUIs
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.langParsed
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.func.waitForEvent
import dev.jaims.moducore.common.message.clearItalics
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.miniToComponent
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent

fun getChatColorGUI(player: Player, plugin: ModuCore): Gui {
    val gui = Gui.gui()
        .type(GuiType.CHEST)
        .rows(5)
        .title(plugin.api.bukkitFileManager.gui[GUIs.CHATCOLOR_TITLE].langParsed.miniStyle().miniToComponent())
        .create()

    val playerData = plugin.api.storageManager.loadPlayerData(player.uniqueId).join()

    gui.setItem(2, 2, WHITE.asGuiItem(player, playerData))
    gui.setItem(2, 3, LIGHT_GRAY.asGuiItem(player, playerData))
    gui.setItem(2, 4, DARK_GRAY.asGuiItem(player, playerData))
    gui.setItem(2, 5, BLACK.asGuiItem(player, playerData))
    gui.setItem(2, 6, BROWN.asGuiItem(player, playerData))
    gui.setItem(2, 7, LIGHT_BLUE.asGuiItem(player, playerData))
    gui.setItem(2, 8, AQUA.asGuiItem(player, playerData))
    gui.setItem(3, 2, DARK_BLUE.asGuiItem(player, playerData))
    gui.setItem(3, 3, PINK.asGuiItem(player, playerData))
    gui.setItem(3, 4, MAGENTA.asGuiItem(player, playerData))
    gui.setItem(3, 5, PURPLE.asGuiItem(player, playerData))
    gui.setItem(3, 6, YELLOW.asGuiItem(player, playerData))
    gui.setItem(3, 7, ORANGE.asGuiItem(player, playerData))
    gui.setItem(3, 8, RED.asGuiItem(player, playerData))
    gui.setItem(4, 4, LIME.asGuiItem(player, playerData))
    gui.setItem(4, 6, GREEN.asGuiItem(player, playerData))

    // custom color
    gui.setItem(4, 5, ItemBuilder.from(Material.OAK_SIGN)
        .name("<rainbow><bold>Custom Color!".miniToComponent().clearItalics())
        .lore(
            "<dark_gray><bold>| <green>Left Click to Give a Custom Value!".miniToComponent().clearItalics(),
            "<dark_gray><bold>| <red>Right Click to Clear!".miniToComponent().clearItalics()
        )
        .asGuiItem {
            if (!Permissions.CC_CUSTOM.has(player)) {
                player.closeInventory()
                return@asGuiItem
            }
            if (it.isLeftClick) {
                player.closeInventory()
                player.send(Lang.CHATCOLOR_PROMPT)
                // Spigot Event
                plugin.waitForEvent<AsyncPlayerChatEvent>(
                    timeoutTicks = (20 * 60).toLong(),
                    predicate = { event -> event.player.uniqueId == player.uniqueId },
                    priority = EventPriority.LOWEST
                ) { event ->
                    event.isCancelled = true
                    playerData.chatColor = event.message
                    player.send(Lang.CHATCOLOR_SUCCESS) { mes -> mes.replace("{color}", event.message) }
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
        lore(REMOVE_LORE)
    } else {
        glow(false)
        lore(SELECT_LORE)
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
    player.send(Lang.CHATCOLOR_SUCCESS) { it.replace("{color}", (playerData.chatColor ?: "")) }
}

private val SELECT_LORE = "<dark_gray><bold>| <green>Click to Select!".miniToComponent().clearItalics()
private val REMOVE_LORE = "<dark_gray><bold>| <red>Click to Remove!".miniToComponent().clearItalics()

class ChatColorItem(
    private val color: String,
    private val colorDisplay: String,
    private val material: Material,
    private val permission: Permissions
) {
    fun asGuiItem(player: Player, playerData: PlayerData) = ItemBuilder.from(material)
        .buildChatColorItem(playerData, color)
        .name("${color}<bold>${colorDisplay}".miniToComponent().decoration(TextDecoration.ITALIC, false))
        .asGuiItem { handleClick(player, playerData, color, permission) }
}


private val WHITE = ChatColorItem("<white>", "White", Material.WHITE_WOOL, Permissions.CC_WHITE)
private val LIGHT_GRAY = ChatColorItem("<gray>", "Light Gray", Material.LIGHT_GRAY_WOOL, Permissions.CC_LIGHTGRAY)
private val DARK_GRAY = ChatColorItem("<dark_gray>", "Dark Gray", Material.GRAY_WOOL, Permissions.CC_GRAY)
private val BLACK = ChatColorItem("<black>", "Black", Material.BLACK_WOOL, Permissions.CC_BLACK)
private val BROWN = ChatColorItem("<#964b00>", "Brown", Material.BROWN_WOOL, Permissions.CC_BROWN)
private val LIGHT_BLUE = ChatColorItem("<aqua>", "Light Blue", Material.LIGHT_BLUE_WOOL, Permissions.CC_LIGHTBLUE)
private val AQUA = ChatColorItem("<dark_aqua>", "Aqua", Material.CYAN_WOOL, Permissions.CC_AQUA)
private val DARK_BLUE = ChatColorItem("<dark_blue>", "Blue", Material.BLUE_WOOL, Permissions.CC_BLUE)
private val PINK = ChatColorItem("<light_purple>", "Pink", Material.PINK_WOOL, Permissions.CC_PINK)
private val MAGENTA = ChatColorItem("<#ff00ff>", "Magenta", Material.MAGENTA_WOOL, Permissions.CC_MAGENTA)
private val PURPLE = ChatColorItem("<dark_purple>", "Purple", Material.PURPLE_WOOL, Permissions.CC_PURPLE)
private val YELLOW = ChatColorItem("<yellow>", "Yellow", Material.YELLOW_WOOL, Permissions.CC_YELLOW)
private val ORANGE = ChatColorItem("<gold>", "Orange", Material.ORANGE_WOOL, Permissions.CC_ORANGE)
private val RED = ChatColorItem("<dark_red>", "Red", Material.RED_WOOL, Permissions.CC_RED)
private val LIME = ChatColorItem("<green>", "Lime", Material.LIME_WOOL, Permissions.CC_LIME)
private val GREEN = ChatColorItem("<dark_green>", "Green", Material.GREEN_WOOL, Permissions.CC_GREEN)