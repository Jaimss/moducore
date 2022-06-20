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

package dev.jaims.moducore.bukkit.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.mcutils.bukkit.func.send
import dev.jaims.mcutils.common.toPastebin
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.func.getLatestVersion
import dev.jaims.moducore.bukkit.func.tps
import dev.jaims.moducore.bukkit.perm.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ModuCoreDumpCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/moducoredump [--no-player-data] [--with-comments]"
    override val description: String = "Dump server information to a pastebin."
    override val commandName: String = "moducoredump"
    override val module: Property<Boolean>? = null

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument("--no-player-data", StringArgumentType.word()))
            .then(RequiredArgumentBuilder.argument("--with-comments", StringArgumentType.word()))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        // perms
        if (!Permissions.DUMP.has(sender)) return

        sender.send("&8[&e!&8] &eDumping...")

        // add the lines
        val lines = mutableListOf<String>().apply {

            add("# ModuCore Server Dump")
            val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")
            add("# " + dateFormatter.format(Date()))
            val executor = if (sender is Player) {
                "${sender.name}(${sender.uniqueId})"
            } else "Console"
            add("# Executor: $executor")

            add("")

            add("###########################")
            add("### SERVER INFO SECTION ###")
            add("###########################")
            add("Paper: ${plugin.isPaper}")
            add("Name: ${plugin.server.name}")
            add("Version: ${plugin.server.version}")
            add("Bukkit Version: ${plugin.server.bukkitVersion}")
            add("ModuCore Version: ${plugin.description.version}")
            add("ModuCore Latest Version: ${getLatestVersion(plugin.resourceId)}")
            add("TPS: ${tps.tps}")
            add("Online Players: ${plugin.server.onlinePlayers.joinToString(", ") { "${it.name}(${it.uniqueId})" }}")
            add("Java Version: ${System.getProperty("java.version")}")
            add("OS Architecture: ${System.getProperty("os.arch")}")
            add("OS Version: ${System.getProperty("os.version")}")
            add("OS Name: ${System.getProperty("os.name")}")

            add("")
            add("#####################")
            add("### FILES SECTION ###")
            add("#####################")

            add("# HOLOGRAM FILES")
            add("# HOLOGRAM FILES")

            hologramManager.hololibManager.cachedHolograms.forEach { holo ->
                hologramManager.saveHologram(holo.name, holo)
            }
            File("${plugin.dataFolder}/hologram/").walk().filter { !it.isDirectory }.forEach { file ->
                add("")
                add("# ${file.name} (HOLOGRAM FILE)")
                val lines = file.readLines()
                    .filter { if (!args.contains("--with-comments")) !it.trimStart().startsWith("#") else true }
                addAll(lines)
            }

            add("# REGULAR FILE")
            add("# REGULAR FILE")

            (fileManager.allFiles + File(plugin.dataFolder, "kits.yml")).forEach { file ->
                add("")
                add("# ${file.name}")
                // remove comments if `--comments` not an argument
                val lines = file.readLines()
                    .filter { if (!args.contains("--with-comments")) !it.trimStart().startsWith("#") else true }
                addAll(lines.filter { !it.contains("discord-bot-token") })
            }

            add("")
            add("###########################")
            add("### PLAYER DATA SECTION ###")
            add("###########################")
            if (!args.contains("--no-player-data")) {
                add("# CACHE")
                addAll(storageManager.playerDataCache.map { "${it.key} ==> ${it.value}" })
            }


        }.joinToString("\n")

        // convert to a paste and send it
        val paste = lines.toPastebin()
        sender.send("&8(&a!&8) &aDump available at &3$paste&a.")
    }
}