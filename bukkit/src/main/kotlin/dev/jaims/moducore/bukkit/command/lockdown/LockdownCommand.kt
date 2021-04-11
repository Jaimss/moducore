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

package dev.jaims.moducore.bukkit.command.lockdown

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.send
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LockdownCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.LOCKDOWN.has(sender)) return

        val group = args.firstOrNull()?.toLowerCase() ?: "default"

        fileManager.config[Config.LOCKDOWN_GROUP] = group
        fileManager.config.save()

        when (group) {
            "none" -> {
                sender.send(Lang.LOCKDOWN_REMOVED)
            }
            else -> {
                sender.send(Lang.LOCKDOWN_SET) { it.replace("{group}", group) }
            }
        }
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(listOf("default", "none"))
            }
        }
    }

    override val module: Property<Boolean> = Modules.LOCKDOWN
    override val usage: String = "/lockdown [group]"
    override val description: String = "Lock the server down to a specific group. \"none\" for no lockdown"
    override val commandName: String = "lockdown"
    override val brigadierSyntax: LiteralArgumentBuilder<*>? = LiteralArgumentBuilder.literal<String>(commandName)
        .then(RequiredArgumentBuilder.argument("group", StringArgumentType.word()))
}