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

package dev.jaims.moducore.bukkit.command.teleport.request

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.command.teleport.data.TeleportRequest
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.send
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportDenyCommand(override val plugin: ModuCore) : BaseCommand {
    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        if (!Permissions.TELEPORT_DENY.has(sender)) return

        // get the request
        val request = TeleportRequest.REQUESTS.firstOrNull { it.target.uniqueId == sender.uniqueId } ?: run {
            sender.send(Lang.TPR_NO_PENDING_REQUESTS, sender)
            return
        }

        TeleportRequest.REQUESTS.remove(request)
        request.sender.send(Lang.TPR_REQUEST_DENIED, request.target)
        request.target.send(Lang.TPR_REQUEST_DENIED_TARGET, request.sender)
    }

    override val module: Property<Boolean> = Modules.COMMAND_TELEPORT
    override val usage: String = "/tpdeny"
    override val description: String = "Deny a teleport request."
    override val commandName: String = "teleportdeny"
    override val aliases: List<String> = listOf("tpdeny")
}