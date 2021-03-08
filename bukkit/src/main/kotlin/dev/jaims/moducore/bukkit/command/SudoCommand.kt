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
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.playerNotFound
import dev.jaims.moducore.bukkit.util.send
import dev.jaims.moducore.bukkit.util.usage
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SudoCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/sudo <target> <command>"
    override val description: String = "Make a player run a command or a message."
    override val commandName: String = "sudo"
    override val module: Property<Boolean> = Modules.COMMAND_SUDO

    override val brigadierSyntax: LiteralArgumentBuilder<*>
        get() = LiteralArgumentBuilder.literal<String>(commandName).then(
            RequiredArgumentBuilder.argument<String, String>("player", StringArgumentType.word())
                .then(RequiredArgumentBuilder.argument("command", StringArgumentType.greedyString()))
        )


    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.SUDO.has(sender)) return
        if (args.size < 2) {
            sender.usage(usage, description)
            return
        }

        val target = playerManager.getTargetPlayer(args[0]) ?: run {
            sender.playerNotFound(args[0])
            return
        }

        target.performCommand(args.drop(1).joinToString(" "))
        sender.send(Lang.SUDO, target) { it.replace("{command}", "/${args.drop(1).joinToString(" ")}") }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(playerManager.getPlayerCompletions(args[0]))
                2 -> addAll(allCommands.map { it.commandName }.filter { it.startsWith(args[1], ignoreCase = true) })
            }
        }

    }

}