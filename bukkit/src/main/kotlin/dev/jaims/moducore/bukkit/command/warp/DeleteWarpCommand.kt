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

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.config.Warps
import dev.jaims.moducore.bukkit.perm.Permissions
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.func.usage
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class DeleteWarpCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/delwarp <name>"
    override val description: String = "Delete a warp."
    override val commandName: String = "deletewarp"
    override val aliases: List<String> = listOf("delwarp")
    override val module: Property<Boolean> = Modules.COMMAND_WARPS

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(
                RequiredArgumentBuilder.argument("name", StringArgumentType.word())
            )

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.DEL_WARP.has(sender)) return

        if (args.size != 1) {
            sender.usage(usage, description)
            return
        }

        val warp = args[0]
        val success = locationManager.deleteWarp(warp)

        if (!success) {
            sender.send(Lang.WARP_NOT_FOUND) { it.replace("{name}", warp) }
        } else {
            sender.send(Lang.WARP_DELETED) { it.replace("{name}", warp) }
        }

    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(fileManager.warps[Warps.WARPS].keys.filter { it.startsWith(args[0], ignoreCase = true) })
            }
        }
    }

}