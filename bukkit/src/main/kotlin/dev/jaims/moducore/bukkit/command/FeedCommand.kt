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
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.perm.Permissions
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.playerNotFound
import dev.jaims.moducore.bukkit.func.usage
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FeedCommand(override val plugin: ModuCore) : BaseCommand {

    override val commandName = "feed"
    override val usage = "/feed [target]"
    override val description = "Feed yourself or a target."
    override val module: Property<Boolean> = Modules.COMMAND_FEED

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word()))

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            0 -> {
                // check perms
                if (!Permissions.FEED.has(sender)) return
                // only players
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }
                playerManager.feedPlayer(sender, props.isSilent)
            }
            1 -> {
                // check perms
                if (!Permissions.FEED_OTHERS.has(sender)) return
                // get target
                val target = plugin.api.playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                playerManager.feedPlayer(target, props.isSilent, sender)
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val completions = mutableListOf<String>()

        when (args.size) {
            1 -> completions.addAll(playerManager.getPlayerCompletions(args[0]))
        }

        return completions
    }

}