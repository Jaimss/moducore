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

package dev.jaims.moducore.bukkit.command.warp

import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import dev.jaims.mcutils.bukkit.event.waitForEvent
import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Warps
import dev.jaims.moducore.bukkit.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent

class WarpCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/warp <name> [target]"
    override val description: String = "Warp to a location."
    override val commandName: String = "warp"

    private val fileManager = plugin.api.fileManager
    private val playerManager = plugin.api.playerManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {

        val cooldown = fileManager.config.getProperty(Config.WARP_COOLDOWN)

        when (args.size)
        {
            0 ->
            {
                if (!Perm.LIST_WARPS.has(sender)) return
                sender.send("&6Warps: ${getAllWarpNames().joinToString(", ")}")
            }
            1 ->
            {
                if (!Perm.WARP.has(sender)) return
                // console cant warp
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }

                // check if they have perms and get the location
                val targetWarp = args[0]
                if (!sender.hasPermission("moducore.warp.${targetWarp.toLowerCase()}"))
                {
                    sender.noPerms("moducore.warp.${targetWarp.toLowerCase()}")
                    return
                }
                val location =
                    fileManager.warps.getProperty(Warps.WARPS).mapKeys { it.key.toLowerCase() }[targetWarp.toLowerCase()]?.location ?: run {
                        sender.send(fileManager.getString(Lang.WARP_NOT_FOUND, sender).replace("{name}", targetWarp))
                        return
                    }

                // a case to bypass cooldown. if one is 0, there will never be cooldown, or they can use the --bypass-cooldown
                if (cooldown == 0 || props.bypassCooldown)
                {
                    sender.send(fileManager.getString(Lang.WARP_TELEPORTED, sender).replace("{name}", targetWarp))
                    sender.teleport(location)
                    return
                }

                // go through normally with a cooldown
                sender.send(
                    fileManager.getString(Lang.WARP_TELEPORTING, sender)
                        .replace("{name}", targetWarp)
                        .replace("{cooldown}", cooldown.toString())
                )

                // start a task to cancel
                val task = plugin.server.scheduler.schedule(plugin, SynchronizationContext.ASYNC) {
                    // we wait the cooldown then switch to sync
                    waitFor((20 * cooldown).toLong())
                    // without the context switch, the teleportation below wont work.
                    switchContext(SynchronizationContext.SYNC)
                    sender.send(fileManager.getString(Lang.WARP_TELEPORTED, sender).replace("{name}", targetWarp))
                    sender.teleport(location)
                }

                // start a move event so if they move we can cancel the teleportation
                cancelOnMove(sender, cooldown, task)
            }
            2 ->
            {
                if (!Perm.WARP_OTHERS.has(sender)) return
                val targetWarp = args[0]
                if (!sender.hasPermission("moducore.warp.${targetWarp.toLowerCase()}"))
                {
                    sender.noPerms("moducore.warp.${targetWarp.toLowerCase()}")
                    return
                }
                val location =
                    fileManager.warps.getProperty(Warps.WARPS).mapKeys { it.key.toLowerCase() }[targetWarp.toLowerCase()]?.location ?: run {
                        sender.send(fileManager.getString(Lang.WARP_NOT_FOUND).replace("{name}", targetWarp))
                        return
                    }

                val targetPlayer = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return
                }

                targetPlayer.teleport(location)
                if (!props.isSilent)
                    targetPlayer.send(fileManager.getString(Lang.WARP_TELEPORTED, targetPlayer).replace("{name}", targetWarp))
                sender.send(fileManager.getString(Lang.WARP_TELEPORTED_TARGET, targetPlayer).replace("{name}", targetWarp))
            }
            else -> sender.usage(usage, description)
        }
    }

    private fun getAllWarpNames(): List<String>
    {
        return fileManager.warps.getProperty(Warps.WARPS).map { it.key }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>
    {
        return mutableListOf<String>().apply {
            when (args.size)
            {
                1 -> addAll(getAllWarpNames().filter { it.startsWith(args[0], ignoreCase = true) })
                2 -> addAll(playerManager.getPlayerCompletions(args[1]))
            }
        }
    }

}
