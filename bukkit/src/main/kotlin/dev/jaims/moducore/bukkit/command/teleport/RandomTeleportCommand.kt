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

package dev.jaims.moducore.bukkit.command.teleport

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Perm
import dev.jaims.moducore.bukkit.util.decimalFormat
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.playerNotFound
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class RandomTeleportCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/randomteleport [target]"
    override val description: String = "Teleport to a random location on the map."
    override val commandName: String = "randomteleport"

    override val commodoreSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(
                RequiredArgumentBuilder.argument("target", StringArgumentType.word())
            )

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        when (args.size)
        {
            0 ->
            {
                if (!Perm.TELEPORT_RANDOM.has(sender)) return
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }
                val loc = getLocation(sender)

                sender.teleport(loc)
                sender.send(
                    fileManager.getString(Lang.TELEPORT_POSITION_SUCCESS, sender)
                        .replace("{x}", decimalFormat.format(loc.x))
                        .replace("{y}", decimalFormat.format(loc.y))
                        .replace("{z}", decimalFormat.format(loc.z))
                        .replace("{world}", loc.world.name)
                )
            }
            1 ->
            {
                if (!Perm.TELEPORT_RANDOM_OTHERS.has(sender)) return

                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }

                val loc = getLocation(target)
                target.teleport(loc)
                if (!props.isSilent)
                {
                    target.send(
                        fileManager.getString(Lang.TELEPORT_POSITION_SUCCESS, target)
                            .replace("{x}", decimalFormat.format(loc.x))
                            .replace("{y}", decimalFormat.format(loc.y))
                            .replace("{z}", decimalFormat.format(loc.z))
                            .replace("{world}", loc.world.name)
                    )
                }
                sender.send(
                    fileManager.getString(Lang.TELEPORT_POSITION_TARGET, target)
                        .replace("{x}", decimalFormat.format(loc.x))
                        .replace("{y}", decimalFormat.format(loc.y))
                        .replace("{z}", decimalFormat.format(loc.z))
                        .replace("{world}", loc.world.name)
                )
            }
        }
    }

    private fun getLocation(player: Player): Location
    {
        val x = Random.nextDouble(-fileManager.config.getProperty(Config.RTP_MAX_X), fileManager.config.getProperty(Config.RTP_MAX_X))
        val z = Random.nextDouble(-fileManager.config.getProperty(Config.RTP_MAX_Z), fileManager.config.getProperty(Config.RTP_MAX_Z))

        return player.location.world.getHighestBlockAt(x.toInt(), z.toInt()).location.add(0.0, 1.1, 0.0)
    }

}