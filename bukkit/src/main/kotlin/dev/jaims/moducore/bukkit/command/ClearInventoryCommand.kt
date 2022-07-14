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
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.bukkit.const.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClearInventoryCommand(override val plugin: ModuCore) : BaseCommand {

    override val usage = "/clear [target]"
    override val description = "Clear your inventory or a targets."
    override val commandName = "clear"
    override val aliases: List<String> = listOf("clearinventory", "ci")
    override val module: Property<Boolean> = Modules.COMMAND_CLEARINVENTORY

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word()))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            0 -> {
                if (!Permissions.CLEAR.has(sender)) return
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }
                sender.inventory.clear()
                sender.send(Lang.INVENTORY_CLEARED, sender)
            }
            1 -> {
                if (!Permissions.CLEAR_OTHERS.has(sender)) return
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                target.inventory.clear()
                if (!props.isSilent) {
                    target.send(Lang.INVENTORY_CLEARED, target)
                }
                sender.send(Lang.TARGET_INVENTORY_CLEARED, target)
            }
            else -> sender.usage(usage, description)
        }
        return
    }

    override suspend fun onTabComplete(
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