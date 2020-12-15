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
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class FileManager(plugin: JCore) {

    private val langFile = File(plugin.dataFolder, "lang.yml")
    lateinit var lang: YamlConfiguration

    /**
     * Create the different files when the filemanager is created
     */
    init {
        plugin.saveDefaultConfig()
        createLangFile()

        enumToFile(Config.values(), plugin.config, File(plugin.dataFolder, "config.yml"))
        enumToFile(Lang.values(), lang, langFile)
    }

    /**
     * Create the language file for the plugin
     */
    private fun createLangFile() {
        if (!langFile.exists()) langFile.createNewFile()
        lang = YamlConfiguration.loadConfiguration(langFile)
    }

    /**
     * Save stuff from an enum to the language file if it doesn't exist.
     */
    private fun <T : ConfigFileEnum> enumToFile(enumValues: Array<T>, file: FileConfiguration, filePath: File) {
        for (e in enumValues) {
            val current = file.get(e.path)
            if (current == null) {
                file.set(e.path, e.default)
                file.save(filePath)
            }
        }
    }
}

/**
 * A config file enum interface that provides a path and a default value of any type.
 */
interface ConfigFileEnum {
    val path: String
    val default: Any
}