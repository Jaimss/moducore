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

package dev.jaims.moducore.bukkit.func

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.miniToComponent
import dev.jaims.moducore.common.message.plainText
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Check if a nickname is valid
 */
fun String?.isValidNickname(): Boolean {
    if (this == null) return true
    val plugin = JavaPlugin.getPlugin(ModuCore::class.java)
    val regex = plugin.api.bukkitFileManager.config[Config.NICKNAME_REGEX]

    // regex check occurs on plain
    val plain = this.miniStyle().miniToComponent().plainText()
    return plain.matches(regex.toRegex())
}

/**
 * @return a string with the PlaceholderAPI placeholders set
 */
fun String.placeholders(player: Player?) = PlaceholderAPI.setPlaceholders(player, this)

/**
 * Parse a string with the lang colors and prefix's
 */
val String.langParsed: String
    get() {
        val lang = JavaPlugin.getPlugin(ModuCore::class.java).api.bukkitFileManager.lang
        var mutableMessage = this
        // replace prefixes
        lang[Lang.PREFIXES].forEach { (k, v) -> mutableMessage = mutableMessage.replace("{prefix_$k}", v) }
        // replace colors
        lang[Lang.COLORS].forEach { (k, v) -> mutableMessage = mutableMessage.replace("{color_$k}", v) }
        return mutableMessage
    }
