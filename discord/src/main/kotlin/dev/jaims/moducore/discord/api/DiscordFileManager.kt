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

package dev.jaims.moducore.discord.api

import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import dev.jaims.moducore.api.manager.FileManager
import dev.jaims.moducore.discord.config.DiscordBot
import dev.jaims.moducore.discord.config.DiscordLang
import dev.jaims.moducore.discord.config.DiscordModules
import dev.jaims.moducore.discord.data.ConfigurableEmbed
import dev.jaims.moducore.discord.data.ConfigurableEmbedField
import dev.jaims.moducore.discord.data.ConfigurableMessage
import me.mattstudios.config.SettingsManager
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class DiscordFileManager(dataFolder: File) : FileManager {
    private val discordLangGson = GsonBuilder()
        .registerTypeAdapter(DiscordLang::class.java, InstanceCreator { DiscordLang() })
        .registerTypeAdapter(ConfigurableMessage::class.java, InstanceCreator { ConfigurableMessage() })
        .registerTypeAdapter(ConfigurableEmbed::class.java, InstanceCreator { ConfigurableEmbed() })
        .registerTypeAdapter(ConfigurableEmbedField::class.java, InstanceCreator { ConfigurableEmbedField() })
        .setPrettyPrinting()
        .create()

    private val discordFile = File(dataFolder, "discord/discord.yml")
    val discord = SettingsManager.from(discordFile).configurationData(DiscordBot::class.java).create()

    private val modulesFile = File(dataFolder, "discord/modules.yml")
    val modules = SettingsManager.from(modulesFile).configurationData(DiscordModules::class.java).create()

    private val discordLangFile = File(dataFolder, "discord/lang.yml")
    var discordLang: DiscordLang

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
    }

    override fun reload() {
        // discord lang
        val reader = FileReader(discordLangFile)
        discordLang = discordLangGson.fromJson(reader, DiscordLang::class.java)
        reader.close()

        discord.reload()
        modules.reload()
    }
}