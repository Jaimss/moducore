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

package dev.jaims.jcore.bukkit.command

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.mcutils.bukkit.log
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import javax.print.attribute.standard.Severity

interface JCoreCommand : CommandExecutor, TabExecutor
{

    // a usage for the command
    val usage: String

    // a description of what the command does
    val description: String

    // the name of the command
    val commandName: String

    /**
     * A method to register a [JCoreCommand]
     */
    fun register(plugin: JCore)
    {
        val cmd = plugin.getCommand(commandName) ?: run {
            plugin.log("Command with name: $commandName is not in the plugin.yml!", Severity.WARNING)
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

val allCommands: MutableList<JCoreCommand> = mutableListOf()