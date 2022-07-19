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

package dev.jaims.moducore.bukkit.listener

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.SpigotOnlyNoSuchMethod
import dev.jaims.moducore.bukkit.func.langParsed
import dev.jaims.moducore.bukkit.func.placeholders
import dev.jaims.moducore.bukkit.func.suggestPaperWarning
import dev.jaims.moducore.common.message.legacyString
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.miniToComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val plugin: ModuCore) : Listener {

    private val fileManager = plugin.api.bukkitFileManager
    private val playtimeManager = plugin.api.playtimeManager
    private val storageManager = plugin.api.storageManager

    /**
     * Quit logic
     */
    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        if (fileManager.modules[Modules.QUIT_MESSAGE]) {
            val langQuitMessage = fileManager.lang[Lang.QUIT_MESSAGE].langParsed
            val colorized = langQuitMessage.placeholders(player).miniStyle().miniToComponent()
            try {
                quitMessage(colorized)
            } catch (ignored: SpigotOnlyNoSuchMethod) {
                plugin.suggestPaperWarning()
                quitMessage = colorized.legacyString()
            }

            // remove the player from the joinTimes map
            playtimeManager.joinTimes.remove(player.uniqueId)

            // remove the player from the data
            // this will also save it
            storageManager.unloadPlayerData(player.uniqueId)
        }
    }
}