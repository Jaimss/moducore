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
import dev.jaims.moducore.bukkit.const.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TimeCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/time <morning|day|afternoon|sunset|night|midnight|number>"
    override val description: String = "Change the time in the world."
    override val commandName: String = "time"
    override val module: Property<Boolean> = Modules.COMMAND_TIME

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(LiteralArgumentBuilder.literal(morning))
            .then(LiteralArgumentBuilder.literal(day))
            .then(LiteralArgumentBuilder.literal(noon))
            .then(LiteralArgumentBuilder.literal(afternoon))
            .then(LiteralArgumentBuilder.literal(sunset))
            .then(LiteralArgumentBuilder.literal(night))
            .then(LiteralArgumentBuilder.literal(midnight))
            .then(RequiredArgumentBuilder.argument("number", IntegerArgumentType.integer(0, 24000)))

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.TIME.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        if (args.size != 1) {
            sender.usage(usage, description)
            return
        }

        val world = sender.world

        when (args[0]) {
            morning -> world.time = 23000
            day -> world.time = 1000
            noon, afternoon -> world.time = 6000
            sunset -> world.time = 12000
            night -> world.time = 13000
            midnight -> world.time = 18000
            else -> world.time = args[0].toLongOrNull() ?: kotlin.run {
                sender.invalidNumber()
                return
            }
        }

        sender.send(Lang.TIME_SUCCESS, sender) { it.replace("{time}", world.time.toString()) }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val possibilities = mutableListOf(morning, day, noon, afternoon, sunset, night, midnight)
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(possibilities.filter { it.startsWith(args[0], ignoreCase = true) })
            }
        }
    }

    private val morning = "morning"
    private val day = "day"
    private val noon = "noon"
    private val afternoon = "afternoon"
    private val sunset = "sunset"
    private val night = "night"
    private val midnight = "midnight"


}