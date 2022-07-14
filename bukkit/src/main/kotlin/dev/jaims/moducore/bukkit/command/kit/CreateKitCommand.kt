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

package dev.jaims.moducore.bukkit.command.kit

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.bukkit.const.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateKitCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.CREATE_KIT.has(sender)) return

        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val name = args.firstOrNull() ?: run {
            sender.usage(usage, description)
            return
        }

        val cooldown = args.getOrNull(1)?.toIntOrNull() ?: run {
            sender.usage(usage, description)
            return
        }

        if (cooldown < 0) {
            sender.send(Lang.NON_POSITIVE_NUMBER)
            return
        }

        val items = with(sender.inventory) {
            contents.filterNotNull()
        }.toList()

        kitManager.createKit(name, cooldown, items, mutableListOf(), mutableListOf())
        sender.send(Lang.KIT_CREATED) {
            it.replace("{name}", name).replace("{cooldown}", cooldown.cooldownFormat).replace("{amount}", items.size.toString())
        }
    }

    override val module: Property<Boolean> = Modules.KITS
    override val usage: String = "/createkit <name> <cooldown>"
    override val description: String = "Create a kit with a name and cooldown."
    override val commandName: String = "createkit"
    override val brigadierSyntax: LiteralArgumentBuilder<*> = LiteralArgumentBuilder.literal<String>(commandName)
        .then(RequiredArgumentBuilder.argument<String, String>("name", StringArgumentType.word())
            .then(RequiredArgumentBuilder.argument("cooldown", IntegerArgumentType.integer(0))))
}