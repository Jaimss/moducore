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

package dev.jaims.moducore.bukkit.message

import dev.jaims.moducore.common.message.SHORT_HEX_PATTERN
import dev.jaims.moducore.common.message.miniToComponent
import dev.jaims.moducore.common.message.rawText
import dev.jaims.moducore.common.message.shortHexPattern
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Convert a &#ff009d hex color into a bungee chatcolor
 */
fun String.hexColor(): net.md_5.bungee.api.ChatColor = net.md_5.bungee.api.ChatColor.of(replace("&", ""))

/**
 * Turn a [Component] into a colorized [String]
 *
 * Keeping this around until spigot decides to work with components
 */
@Deprecated(
    "Deprecated in favor of components",
    ReplaceWith("rawText().legacyColorize(player)", "dev.jaims.moducore.common.message.rawText"),
)
fun Component.legacyColorize(player: Player? = null): String = rawText().legacyColorize(player)

/**
 * @param player the player to set placeholders for
 *
 * @return a colorized [String]
 */
@Deprecated(
    "Deprecated in favor of components",
    ReplaceWith("String#colorize()", "dev.jaims.moducore.common.message.colorize")
)
fun String.legacyColorize(player: Player? = null): String {
    val message = shortHexPattern().replace(SHORT_HEX_PATTERN) {
        return@replace it.value.hexColor().toString()
    }
    return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, message))
}


/**
 * @param player the player to parse PlaceholderAPI placeholders for
 *
 * @return the correct colorized component
 */
fun String.colorize(player: Player? = null) = miniToComponent { PlaceholderAPI.setPlaceholders(player, this) }
