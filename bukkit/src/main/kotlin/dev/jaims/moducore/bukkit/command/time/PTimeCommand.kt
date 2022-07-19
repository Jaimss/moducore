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

package dev.jaims.moducore.bukkit.command.time

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.bukkit.perm.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PTimeCommand(override val plugin: ModuCore) : BaseCommand {

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.PTIME.has(sender)) return

        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val timeStr = args.firstOrNull()?.lowercase() ?: run {
            sender.usage(usage, description)
            return
        }

        if (timeStr == "reset") {
            sender.resetPlayerTime()
            sender.send(Lang.PTIME_RESET)
            return
        }

        val time = when (timeStr) {
            morning -> 23000
            day -> 1000
            noon, afternoon -> 6000
            sunset -> 12000
            night -> 13000
            midnight -> 18000
            else -> timeStr.toLongOrNull() ?: kotlin.run {
                sender.invalidNumber()
                return
            }
        }

        sender.setPlayerTime(time, true)
        sender.send(Lang.PTIME_SUCCESS) { it.replace("{time}", time.toString()) }
    }

    override val module: Property<Boolean> = Modules.COMMAND_PTIME
    override val usage: String = "/ptime <reset|time>"
    override val description: String = "Set your personal time."
    override val commandName: String = "ptime"
    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(LiteralArgumentBuilder.literal(morning))
            .then(LiteralArgumentBuilder.literal(day))
            .then(LiteralArgumentBuilder.literal(noon))
            .then(LiteralArgumentBuilder.literal(afternoon))
            .then(LiteralArgumentBuilder.literal(sunset))
            .then(LiteralArgumentBuilder.literal(night))
            .then(LiteralArgumentBuilder.literal(midnight))
            .then(LiteralArgumentBuilder.literal("reset"))
            .then(RequiredArgumentBuilder.argument("number", IntegerArgumentType.integer(0, 24000)))

    private val morning = "morning"
    private val day = "day"
    private val noon = "noon"
    private val afternoon = "afternoon"
    private val sunset = "sunset"
    private val night = "night"
    private val midnight = "midnight"

}