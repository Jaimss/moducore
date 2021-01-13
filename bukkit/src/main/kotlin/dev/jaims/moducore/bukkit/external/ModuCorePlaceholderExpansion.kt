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

package dev.jaims.moducore.bukkit.external

import dev.jaims.mcutils.bukkit.util.log
import dev.jaims.mcutils.common.toTimeFormatted
import dev.jaims.moducore.api.manager.shortPlaceholder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Placeholders
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import javax.print.attribute.standard.Severity

class ModuCorePlaceholderExpansion(private val plugin: ModuCore) : PlaceholderExpansion()
{

    init
    {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null)
        {
            "PlaceholderAPI Not Found! This is a dependency of ModuCore! Please download it from https://www.spigotmc.org/resources/6245/. Disabling Plugin!"
                .log(Severity.ERROR)
            Bukkit.getPluginManager().disablePlugin(plugin)
        }
    }

    override fun getIdentifier() = "moducore"
    override fun getAuthor() = plugin.description.authors[0] ?: "Jaimss"
    override fun getVersion() = plugin.description.version
    override fun canRegister() = true
    override fun persist() = true

    private val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager
    private val playtimeManager = plugin.api.playtimeManager

    override fun onPlaceholderRequest(player: Player?, id: String): String
    {
        if (player == null) return ""

        when (id)
        {
            "displayname" -> return playerManager.getName(player.uniqueId)

            // playtime placeholders
            "online_since" ->
            {
                val times =
                    playtimeManager.getTimeOnlineSinceJoin(player.uniqueId)?.toTimeFormatted()?.filter { it.value != 0 }
                        ?: mutableMapOf()
                var s = ""
                times.forEach {
                    s += "${it.value}${it.key.shortPlaceholder} "
                }
                return s.trim()
            }
        }

        // custom placeholders
        fileManager.placeholders?.getProperty(Placeholders.PLACEHOLDERS)?.forEach { (placeholder, replacement) ->
            if (id == placeholder) return PlaceholderAPI.setPlaceholders(player, replacement)
        }

        return "null"
    }
}