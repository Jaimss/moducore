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

package dev.jaims.moducore.bukkit.command.speed

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.*
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpeedCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/speed <amount> [target]"
    override val description: String = "Change your speed. Will change flyspeed if flying, and walkspeed if walking."
    override val commandName: String = "speed"
    override val module: Property<Boolean> = Modules.COMMAND_SPEED

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument<String, Int>("amount", IntegerArgumentType.integer(0, 10))
                .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word())))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            1 -> {
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }
                when (sender.isFlying) {
                    true -> {
                        if (!Permissions.FLYSPEED.has(sender)) return
                        val speed = args[0].toIntOrNull() ?: run {
                            sender.invalidNumber()
                            return
                        }
                        playerManager.setFlySpeed(sender, speed, props.isSilent)
                    }
                    false -> {
                        if (!Permissions.WALKSPEED.has(sender)) return
                        val speed = args[0].toIntOrNull() ?: run {
                            sender.invalidNumber()
                            return
                        }
                        playerManager.setWalkSpeed(sender, speed, props.isSilent)
                    }
                }
            }
            2 -> {
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return
                }
                when (target.isFlying) {
                    true -> {
                        // flying
                        if (!Permissions.FLYSPEED_OTHERS.has(sender)) return
                        val speed = args[0].toIntOrNull() ?: run {
                            sender.invalidNumber()
                            return
                        }
                        playerManager.setFlySpeed(target, speed, props.isSilent, sender)
                    }
                    false -> {
                        // not flying
                        if (!Permissions.WALKSPEED_OTHERS.has(sender)) return
                        val speed = args[0].toIntOrNull() ?: run {
                            sender.invalidNumber()
                            return
                        }
                        playerManager.setWalkSpeed(target, speed, props.isSilent, sender)
                    }
                }
            }
            else -> sender.usage(usage, description)
        }
    }

    override suspend fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val completions = mutableListOf<String>()

        when (args.size) {
            1 -> (0..10).forEach {
                if (it.toString().contains(args[0], ignoreCase = true)) completions.add(it.toString())
            }
            2 -> completions.addAll(playerManager.getPlayerCompletions(args[1]))
        }

        return completions
    }


}