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

package dev.jaims.jcore.bukkit.event.listener

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.manager.config.Modules
import dev.jaims.jcore.bukkit.manager.config.SignCommands
import dev.jaims.mcutils.bukkit.colorize
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener(private val plugin: JCore) : Listener {

    private val fileManager = plugin.api.fileManager

    @EventHandler
    fun PlayerInteractEvent.onInteract() {
        println("inside")

        if (fileManager.modules.getProperty(Modules.SIGN_COMMANDS)) {
            println("innerside")
            if (!Perm.SIGN_COMMANDS.has(player)) return
            println("has perms")
            if (clickedBlock == null) return
            println("not null")
            if (clickedBlock!!.state !is Sign) return
            println("clicked block")
            val lines = (clickedBlock!!.state as Sign).lines
            fileManager.signCommands?.getProperty(SignCommands.PLAYER_COMMANDS)?.forEach { (firstLine, command) ->
                println("${lines[0]} -> $firstLine : $command")
                if (ChatColor.stripColor(lines[0]).equals("[$firstLine]", ignoreCase = true)) {
                    println("Inside if in loop")
                    player.performCommand(command.colorize(player))
                }
            }
            fileManager.signCommands?.getProperty(SignCommands.CONSOLE_COMMANDS)?.forEach { (firstLine, command) ->
                println("${lines[0]} -> $firstLine : $command")
                if (ChatColor.stripColor(lines[0]).equals("[$firstLine]", ignoreCase = true)) {
                    println("Inside if in loop 2")
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.colorize(player))
                }
            }

        }

    }


}