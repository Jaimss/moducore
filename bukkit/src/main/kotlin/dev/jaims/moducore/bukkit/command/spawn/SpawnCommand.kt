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

package dev.jaims.moducore.bukkit.command.spawn

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.bukkit.const.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/spawn [target]"
    override val description: String = "Send yourself or a player to spawn."
    override val commandName: String = "spawn"
    override val module: Property<Boolean> = Modules.SPAWN

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word()))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        when (args.size) {
            0 -> {
                if (!Permissions.SPAWN.has(sender)) return
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }

                val cooldown = fileManager.config[Config.SPAWN_COOLDOWN]

                // a case to bypass cooldown. if one is 0, there will never be cooldown, or they can use the --bypass-cooldown
                if (cooldown == 0 || props.bypassCooldown) {
                    sender.send(Lang.SPAWN_TELEPORTED, sender)
                    playerManager.teleportToSpawn(sender)
                    return
                }

                // go through normally with a cooldown
                sender.send(Lang.SPAWN_TELEPORTING, sender) { it.replace("{cooldown}", cooldown.toString()) }

                val task = plugin.server.scheduler.schedule(plugin, SynchronizationContext.ASYNC) {
                    // we wait the cooldown then switch to sync
                    waitFor((20 * cooldown).toLong())
                    // without the context switch, the teleportation below wont work.
                    switchContext(SynchronizationContext.SYNC)
                    sender.send(Lang.SPAWN_TELEPORTED, sender)
                    playerManager.teleportToSpawn(sender)
                }

                // start a move event so if they move we can cancel the teleportation
                cancelTeleportationOnMove(sender, cooldown, task, plugin)
            }
            1 -> {
                if (!Permissions.SPAWN_OTHERS.has(sender)) return
                val target = playerManager.getTargetPlayer(args[0]) ?: kotlin.run {
                    sender.playerNotFound(args[0])
                    return
                }

                playerManager.teleportToSpawn(target)
                if (!props.isSilent) {
                    target.send(Lang.SPAWN_TELEPORTED, target)
                }
                sender.send(Lang.SPAWN_TELEPORTED_TARGET, target)
            }
            else -> sender.usage(usage, description)
        }
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(playerManager.getPlayerCompletions(args[0]))
            }
        }
    }

}
