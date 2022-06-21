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

import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.discord.config.DiscordBot
import dev.jaims.moducore.bukkit.discord.config.DiscordLang
import dev.jaims.moducore.bukkit.discord.data.ConfigurableEmbed
import dev.jaims.moducore.bukkit.discord.data.ConfigurableEmbedField
import dev.jaims.moducore.bukkit.discord.data.ConfigurableMessage
import me.mattstudios.config.SettingsManager
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileManager(private val plugin: ModuCore) {

    // discord Lang Gson
    private val discordLangGson = GsonBuilder()
        .registerTypeAdapter(DiscordLang::class.java, InstanceCreator { DiscordLang() })
        .registerTypeAdapter(ConfigurableMessage::class.java, InstanceCreator { ConfigurableMessage() })
        .registerTypeAdapter(ConfigurableEmbed::class.java, InstanceCreator { ConfigurableEmbed() })
        .registerTypeAdapter(ConfigurableEmbedField::class.java, InstanceCreator { ConfigurableEmbedField() })
        .setPrettyPrinting()
        .create()

    // setup files
    private val configFile = File(plugin.dataFolder, "config.yml")
    val config = SettingsManager.from(configFile).configurationData(Config::class.java).create()

    private val guiFile = File(plugin.dataFolder, "guis.yml")
    val gui = SettingsManager.from(guiFile).configurationData(GUIs::class.java).create()

    // lang
    private val langFile = File(plugin.dataFolder, "lang/lang_${config[Config.LANG_FILE]}.yml")
    val lang = SettingsManager.from(langFile).configurationData(Lang::class.java).create()

    // modules
    private val modulesFile = File(plugin.dataFolder, "modules.yml")
    val modules = SettingsManager.from(modulesFile).configurationData(Modules::class.java).create()

    // signs
    private val signCommandsFile = File(plugin.dataFolder, "sign_commands.yml")
    var signCommands: SettingsManager? = null

    // placeholders
    private val placeholdersFile = File(plugin.dataFolder, "placeholders.yml")
    var placeholders: SettingsManager? = null

    // warps
    private val warpsFile = File(plugin.dataFolder, "warps.yml")
    val warps = SettingsManager.from(warpsFile).configurationData(Warps::class.java).create()

    // discord
    private val discordFile = File(plugin.dataFolder, "discord/discord.yml")
    val discord = SettingsManager.from(discordFile).configurationData(DiscordBot::class.java).create()

    private val discordLangFile = File(plugin.dataFolder, "discord/lang.yml")
    var discordLang: DiscordLang

    // all files
    val allFiles =
        listOf(configFile, langFile, modulesFile, signCommandsFile, placeholdersFile, warpsFile, discordFile, guiFile)

    init {
        // discord lang
        if (!discordLangFile.exists()) {
            discordLangFile.parentFile.mkdirs()
            discordLangFile.createNewFile()
            val writer = FileWriter(discordLangFile)
            discordLangGson.toJson(DiscordLang(), writer)
            writer.close()
        }
        val reader = FileReader(discordLangFile)
        discordLang = discordLangGson.fromJson(reader, DiscordLang::class.java)
        reader.close()

        // module dependent
        if (modules[Modules.PLACEHOLDERS])
            placeholders = SettingsManager.from(placeholdersFile).configurationData(Placeholders::class.java).create()
        if (modules[Modules.SIGN_COMMANDS])
            signCommands = SettingsManager.from(signCommandsFile).configurationData(SignCommands::class.java).create()
    }

    /**
     * reload all config style files
     */
    fun reload() {
        config.reload()
        lang.reload()
        modules.reload()
        gui.reload()
        // discord lang
        val reader = FileReader(discordLangFile)
        discordLang = discordLangGson.fromJson(reader, DiscordLang::class.java)
        reader.close()
        // module based
        if (modules[Modules.SIGN_COMMANDS]) {
            if (signCommands == null) {
                signCommands = SettingsManager.from(File(plugin.dataFolder, "sign_commands.yml"))
                    .configurationData(SignCommands::class.java)
                    .create()
            }
            signCommands?.reload()
        }
        if (modules[Modules.PLACEHOLDERS]) {
            if (placeholders == null) {
                placeholders = SettingsManager.from(File(plugin.dataFolder, "placeholders.yml"))
                    .configurationData(Placeholders::class.java)
                    .create()
            }
            placeholders?.reload()
        }
    }
}