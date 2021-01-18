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

package dev.jaims.moducore.bukkit.config

import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.moducore.bukkit.ModuCore
import me.mattstudios.config.SettingsManager
import me.mattstudios.config.properties.Property
import org.bukkit.entity.Player
import java.io.File

class FileManager(private val plugin: ModuCore) {

    // setup files
    private val configFile = File(plugin.dataFolder, "config.yml")
    val config = SettingsManager.from(configFile).configurationData(Config::class.java).create()

    private val langFile = File(plugin.dataFolder, "lang/lang_${config.getProperty(Config.LANG_FILE)}.yml")
    val lang = SettingsManager.from(langFile).configurationData(Lang::class.java).create()

    private val modulesFile = File(plugin.dataFolder, "modules.yml")
    val modules = SettingsManager.from(modulesFile).configurationData(Modules::class.java).create()

    private val signCommandsFile = File(plugin.dataFolder, "sign_commands.yml")
    var signCommands: SettingsManager? = null

    private val placeholdersFile = File(plugin.dataFolder, "placeholders.yml")
    var placeholders: SettingsManager? = null

    private val warpsFile = File(plugin.dataFolder, "warps.yml")
    val warps = SettingsManager.from(warpsFile).configurationData(Warps::class.java).create()

    val allFiles = listOf(configFile, langFile, modulesFile, signCommandsFile, placeholdersFile, warpsFile)

    init {
        if (modules.getProperty(Modules.PLACEHOLDERS))
            placeholders = SettingsManager.from(placeholdersFile).configurationData(Placeholders::class.java).create()
        if (modules.getProperty(Modules.SIGN_COMMANDS))
            signCommands = SettingsManager.from(signCommandsFile).configurationData(SignCommands::class.java).create()
    }

    /**
     * reload all config style files
     */
    fun reload() {
        config.reload()
        lang.reload()
        modules.reload()
        if (modules.getProperty(Modules.SIGN_COMMANDS)) {
            if (signCommands == null) {
                signCommands = SettingsManager.from(File(plugin.dataFolder, "sign_commands.yml"))
                    .configurationData(SignCommands::class.java)
                    .create()
            }
            signCommands?.reload()
        }
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
    fun getString(
        property: Property<String>,
        player: Player? = null,
        manager: SettingsManager = lang,
        colored: Boolean = true
    ): String {
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