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

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.command.teleport.data.TeleportRequest
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.*
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class TeleportRequestCommand(override val plugin: ModuCore) : BaseCommand {
    /**
     * The method to execute a command.
     *
     * @param sender the sender of the command
     * @param args the list of arguments that were provided by the player
     * @param props the [CommandProperties]
     */
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        if (!Permissions.TELEPORT_REQUEST.has(sender)) return

        val targetName = args.getOrNull(0) ?: run {
            sender.usage(usage, description)
            return
        }

        val target = playerManager.getTargetPlayer(targetName) ?: run {
            sender.playerNotFound(targetName)
            return
        }

        val bypassCooldown = props.bypassCooldown && Permissions.TELEPORT_REQUEST_BYPASSCOOLDOWN.has(sender, false)

        val req = TeleportRequest(sender, target, plugin, Date(), bypassCooldown)
        if (TeleportRequest.REQUESTS.contains(req)) {
            sender.send(Lang.TPR_ALREADY_SENT_TO_PLAYER, target)
            return
        }
        TeleportRequest.REQUESTS.add(req)

        target.send(Lang.TPR_REQUEST_RECEIVED, sender)
        sender.send(Lang.TPR_TELEPORT_REQUEST_SENT, target)
    }

    override val module: Property<Boolean> = Modules.COMMAND_TELEPORT
    override val usage: String = "/tpa <player>"
    override val description: String = "Request to teleport to a player."
    override val commandName: String = "teleportrequest"
    override val aliases: List<String> = listOf("tpa", "tpr")
    override val brigadierSyntax: LiteralArgumentBuilder<*> = LiteralArgumentBuilder.literal<String>(commandName)
        .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word()))
}