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

import dev.jaims.mcutils.bukkit.util.log
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.util.Perm
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import javax.print.attribute.standard.Severity

interface BaseCommand : CommandExecutor, TabExecutor
{

    /**
     * The method to execute a command.
     *
     * @param sender the sender of the command
     * @param args the list of arguments that were provided by the player
     * @param props the [CommandProperties]
     */
    fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)

    val plugin: ModuCore

    // a usage for the command
    val usage: String

    // a description of what the command does
    val description: String

    // the name of the command
    val commandName: String

    /**
     * override the default `onCommand`. it will call the new
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        // send args as alist
        val newArgs = args.toMutableList()

        // determine if its silent
        // if ALERT_TARGET = false, they dont want to alert the target, so silent is true
        // if the args contains "-s", they dont want to alert, so silent is true and we remove "-s"
        var silent = false
        if (!plugin.api.fileManager.config.getProperty(Config.ALERT_TARGET)) silent = true
        if (newArgs.remove("-s"))
        {
            if (Perm.SILENT_COMMAND.has(sender, false)) silent = true
        }
        if (newArgs.remove("--silent"))
        {
            if (Perm.SILENT_COMMAND.has(sender, false)) silent = true
        }

        // confirmation
        var isConfirmation = false
        if (newArgs.remove("-c")) isConfirmation = true
        if (newArgs.remove("--confirm")) isConfirmation = true

        // execute and return true cause we handle all messages
        execute(sender, newArgs, CommandProperties(silent, isConfirmation))
        return true
    }

    /**
     * A method to register a [BaseCommand]
     */
    fun register(plugin: ModuCore)
    {
        val cmd = plugin.getCommand(commandName) ?: run {
            "Command with name: $commandName is not in the plugin.yml!".log(Severity.ERROR)
            return
        }
        cmd.setExecutor(this)
    }

    /**
     * Tab complete isn't required, so it defaults to nothing, but it is available.
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>
    {
        return mutableListOf()
    }

}

/**
 * A command properties class that lets us pass things to the [BaseCommand] execute method.
 */
data class CommandProperties(
    val silent: Boolean,
    val isConfirmation: Boolean,
)

/**
 * A list of all commands on the server for easy registration & help pages.
 */
val allCommands: MutableList<BaseCommand> = mutableListOf()
