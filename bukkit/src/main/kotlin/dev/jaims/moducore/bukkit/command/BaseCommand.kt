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

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.jaims.mcutils.bukkit.func.log
import dev.jaims.moducore.api.manager.*
import dev.jaims.moducore.api.manager.location.LocationManager
import dev.jaims.moducore.api.manager.player.PlayerManager
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.FileManager
import dev.jaims.moducore.bukkit.perm.Permissions
import me.lucko.commodore.CommodoreProvider
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.plugin.Plugin
import javax.print.attribute.standard.Severity

interface BaseCommand : TabCompleter, CommandExecutor {

    /**
     * The method to execute a command.
     *
     * @param sender the sender of the command
     * @param args the list of arguments that were provided by the player
     * @param props the [CommandProperties]
     */
    fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)

    val plugin: ModuCore

    val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = null

    // the boolean property from [Modules] that this command is a part of
    val module: Property<Boolean>?

    // references to the managers for easy access
    val fileManager: FileManager
        get() = plugin.api.fileManager
    val economyManager: EconomyManager
        get() = plugin.api.economyManager
    val playerManager: PlayerManager
        get() = plugin.api.playerManager
    val playtimeManager: PlaytimeManager
        get() = plugin.api.playtimeManager
    val storageManager: StorageManager
        get() = plugin.api.storageManager
    val locationManager: LocationManager
        get() = plugin.api.locationManager
    val hologramManager: HologramManager
        get() = plugin.api.hologramManager
    val kitManager: KitManager
        get() = plugin.api.kitManager

    // a usage for the command
    val usage: String

    // a description of what the command does
    val description: String

    // the name of the command
    val commandName: String

    // the aliases
    val aliases: List<String>
        get() = listOf()

    /**
     * override the default `onCommand`. it will call the new
     */
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // send args as alist
        val newArgs = args.toMutableList()

        // determine if its silent
        // if ALERT_TARGET = false, they dont want to alert the target, so silent is true
        // if the args contains "-s", they dont want to alert, so silent is true and we remove "-s"
        var silent = false
        if (!plugin.api.fileManager.config[Config.ALERT_TARGET]) silent = true
        if (newArgs.remove("-s") || newArgs.remove("--silent")) {
            if (Permissions.SILENT_COMMAND.has(sender, false)) silent = true
        }

        // confirmation
        var isConfirmation = false
        if (newArgs.remove("-c") || newArgs.remove("--confirm")) isConfirmation = true

        // bypass cooldowns
        var bypassCooldown = false
        if (!fileManager.config[Config.COOLDOWN_BYPASS_REQUIRE_ARGUMENT]) {
            if (Permissions.BYPASS_COOLDOWN.has(sender, false)) bypassCooldown = true
        }
        if (newArgs.remove("-bc") || newArgs.remove("--bypass-cooldown")) {
            if (Permissions.BYPASS_COOLDOWN.has(sender, true)) bypassCooldown = true
        }

        // execute and return true cause we handle all messages
        execute(sender, newArgs, CommandProperties(silent, isConfirmation, bypassCooldown))
        return true
    }

    /**
     * A method to register a [BaseCommand]
     */
    fun register(plugin: ModuCore) {
        val command = object : Command(commandName) {
            val com = this
            override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                onCommand(sender, com, commandLabel, args)
                return true
            }

            override fun tabComplete(
                sender: CommandSender,
                alias: String,
                args: Array<out String>
            ): MutableList<String> {
                return onTabComplete(sender, com, alias, args)
            }
        }
        val tempAliases = aliases.toMutableList()
        tempAliases.addAll(aliases.map { "mc$it" })
        tempAliases.add("mc${commandName}")
        command.aliases = tempAliases
        if (CommodoreProvider.isSupported() && brigadierSyntax != null) {
            val commodore = CommodoreProvider.getCommodore(plugin)
            commodore.register(command, brigadierSyntax!!.build())
        }
        command.registerPluginYml(plugin)
    }

    private fun Command.registerPluginYml(plugin: Plugin) {
        try {
            val getCmdMap = Bukkit.getServer()::class.java.getDeclaredMethod("getCommandMap")
            getCmdMap.isAccessible = true
            val commandMap = getCmdMap.invoke(Bukkit.getServer()) as CommandMap

            val oldcmd = commandMap.getCommand(name)
            if (oldcmd is PluginIdentifiableCommand && oldcmd.plugin == plugin) {
                oldcmd.unregister(commandMap)
            }

            commandMap.register(plugin.name, this)
        } catch (e: NoSuchMethodError) {
            "The `getCommandMap` method was not found, so the ${this.name} command couldn't be registered! Please let James know at https://discord.jaims.dev"
                .log(Severity.ERROR)
        }
    }

    /**
     * Tab complete isn't required, so it defaults to nothing, but it is available.
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return mutableListOf()
    }

}

/**
 * A command properties class that lets us pass things to the [BaseCommand] execute method.
 */
data class CommandProperties(
    val isSilent: Boolean,
    val isConfirmation: Boolean,
    val bypassCooldown: Boolean
)

/**
 * A list of all commands on the server for easy registration & help pages.
 */
val allCommands: MutableList<BaseCommand> = mutableListOf()
