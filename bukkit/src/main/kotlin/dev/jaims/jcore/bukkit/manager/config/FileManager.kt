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

package dev.jaims.jcore.bukkit.manager.config

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.mcutils.bukkit.colorize
import me.mattstudios.config.SettingsManager
import me.mattstudios.config.properties.Property
import org.bukkit.entity.Player
import java.io.File

class FileManager(private val plugin: JCore) {

    // setup files
    val config = SettingsManager.from(File(plugin.dataFolder, "config.yml"))
        .configurationData(Config::class.java)
        .create()
    val lang = SettingsManager.from(File(plugin.dataFolder, "lang/lang_${config.getProperty(Config.LANG_FILE)}.yml"))
        .configurationData(Lang::class.java)
        .create()
    val modules = SettingsManager.from(File(plugin.dataFolder, "modules.yml"))
        .configurationData(Modules::class.java)
        .create()
    var placeholders: SettingsManager? = null

    init {
        if (modules.getProperty(Modules.PLACEHOLDERS)) {
            placeholders = SettingsManager.from(File(plugin.dataFolder, "placeholders.yml"))
                .configurationData(Placeholders::class.java)
                .create()
        }
    }

    /**
     * reload all config style files
     */
    fun reload() {
        config.reload()
        lang.reload()
        modules.reload()
        if (modules.getProperty(Modules.PLACEHOLDERS)) {
            if (placeholders == null) {
                placeholders = SettingsManager.from(File(plugin.dataFolder, "placeholders.yml"))
                    .configurationData(Placeholders::class.java)
                    .create()
            }
            placeholders?.reload()
        }
    }

    /**
     * Get a message from the enum.
     * Will parse placeholders for a [player] if provided.
     */
    fun getString(property: Property<String>, player: Player? = null, manager: SettingsManager = lang, colored: Boolean = true): String {
        var m = manager.getProperty(property)

        lang.getProperty(Lang.PREFIXES).forEach { (k, v) ->
            m = m.replace("{prefix_$k}", v)
        }
        lang.getProperty(Lang.COLORS).forEach { (k, v) ->
            m = m.replace("{color_$k}", v)
        }
        return if (colored) m.colorize(player) else m
    }

}