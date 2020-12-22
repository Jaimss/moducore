/*
 * This file is a part of JCore, licensed under the MIT License.
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

package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.config.Placeholders
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class JCorePAPIExpansion(private val plugin: JCore) : PlaceholderExpansion() {

    override fun getIdentifier() = "jcore"
    override fun getAuthor() = plugin.description.authors[0] ?: "Jaimss"
    override fun getVersion() = plugin.description.version
    override fun canRegister() = true
    override fun persist() = true

    val playerManager = plugin.api.playerManager
    val fileManager = plugin.api.fileManager

    override fun onPlaceholderRequest(player: Player?, id: String): String {
        if (player == null) return ""

        when (id) {
            "displayname" -> return playerManager.getName(player.uniqueId)
        }

        // custom placeholders
        fileManager.placeholders?.getProperty(Placeholders.PLACEHOLDERS)?.forEach { (placeholder, replacement) ->
            if (id == placeholder) return PlaceholderAPI.setPlaceholders(player, replacement)
        }

        return "null"
    }
}