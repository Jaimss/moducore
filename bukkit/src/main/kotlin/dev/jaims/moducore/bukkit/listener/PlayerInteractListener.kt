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

package dev.jaims.moducore.bukkit.listener

import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.moducore.api.event.ModuCoreSignCommandEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.config.SignCommands
import dev.jaims.moducore.bukkit.perm.Permissions
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener(private val plugin: ModuCore) : Listener {

    private val fileManager = plugin.api.fileManager

    /**
     * Handle the interact event
     */
    @EventHandler
    fun PlayerInteractEvent.onInteract() {

        // sign commands
        if (fileManager.modules[Modules.SIGN_COMMANDS]) {
            if (!Permissions.SIGN_COMMANDS.has(player, false)) return
            if (clickedBlock == null) return
            if (clickedBlock!!.state !is Sign) return
            val sign = clickedBlock!!.state as Sign
            val lines = sign.lines
            // run the signCommands for a player
            for ((firstLine, command) in fileManager.signCommands?.get(SignCommands.PLAYER_COMMANDS)
                ?: mutableMapOf()) {
                if (ChatColor.stripColor(lines[0]).equals("[$firstLine]", ignoreCase = true)) {
                    // event
                    val moduCoreSignCommandEvent =
                        ModuCoreSignCommandEvent(player, command, command.colorize(player), sign, this)
                    plugin.server.pluginManager.callEvent(moduCoreSignCommandEvent)

                    // run the command if the event is not cancelled
                    if (moduCoreSignCommandEvent.isCancelled) continue
                    player.performCommand(command.colorize(player))
                }
            }
            // run the console signCommands
            for ((firstLine, command) in fileManager.signCommands?.get(SignCommands.CONSOLE_COMMANDS)
                ?: mutableMapOf()) {
                if (ChatColor.stripColor(lines[0]).equals("[$firstLine]", ignoreCase = true)) {
                    // setup the event
                    val moduCoreSignCommandEvent =
                        ModuCoreSignCommandEvent(
                            Bukkit.getConsoleSender(),
                            command,
                            command.colorize(player),
                            sign,
                            this
                        )
                    plugin.server.pluginManager.callEvent(moduCoreSignCommandEvent)

                    // only run if not cancelled
                    if (moduCoreSignCommandEvent.isCancelled) continue
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.colorize(player))

                }
            }
        }
    }


}