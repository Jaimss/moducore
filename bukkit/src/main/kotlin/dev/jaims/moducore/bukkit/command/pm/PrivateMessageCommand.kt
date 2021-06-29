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

package dev.jaims.moducore.bukkit.command.pm

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.bukkit.perm.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PrivateMessageCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.PRIVATE_MESSAGE_SEND.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val targetName = args.getOrNull(0) ?: run {
            sender.usage(usage, description)
            return
        }
        val target = playerManager.getTargetPlayer(targetName) ?: run {
            sender.playerNotFound(targetName)
            return
        }

        val message = args.drop(1).joinToString(" ")
        sender.send(Lang.PRIVATE_MESSAGE_SENT_FORMAT, target) { it.replace("{message}", message) }
        target.send(Lang.PRIVATE_MESSAGE_RECEIVED_FORMAT, sender) { it.replace("{message}", message) }
        SPIERS.forEach { uuid ->
            Bukkit.getPlayer(uuid)?.send(Lang.SOCIAL_SPY_FORMAT) {
                it.replace("{sender}", sender.name).replace("{target}", targetName).replace("{message}", message)
            }
        }
    }

    override val module: Property<Boolean> = Modules.COMMAND_PMS
    override val usage: String = "/pm <target> <message>"
    override val description: String = "Send a private message."
    override val commandName: String = "pm"
    override val aliases: List<String> = listOf("privatemessage", "msg", "message")
    override val brigadierSyntax: LiteralArgumentBuilder<*> = LiteralArgumentBuilder.literal<String>(commandName)
        .then(RequiredArgumentBuilder.argument<String, String>("target", StringArgumentType.word())
            .then(RequiredArgumentBuilder.argument("message", StringArgumentType.greedyString())))

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            if (args.size == 1) {
                addAll(Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0]) })
            }
        }
    }
}