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

package dev.jaims.moducore.bukkit.command.weather

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.perm.Permissions
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.func.usage
import me.mattstudios.config.properties.Property
import org.bukkit.WeatherType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PWeatherCommand(override val plugin: ModuCore) : BaseCommand {

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.PWEATHER.has(sender)) return

        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val weather = args.firstOrNull()?.lowercase() ?: run {
            sender.usage(usage, description)
            return
        }

        if (weather == "reset") {
            sender.resetPlayerWeather()
            sender.send(Lang.PWEATHER_RESET)
            return
        }

        when (weather) {
            "day", "sun" -> sender.setPlayerWeather(WeatherType.CLEAR)
            "rain", "storm" -> sender.setPlayerWeather(WeatherType.DOWNFALL)
        }
        sender.send(Lang.PWEATHER_SUCCESS) { it.replace("{weather}", weather) }
    }

    override val module: Property<Boolean> = Modules.COMMAND_PWEATHER
    override val usage: String = "/pweather <weather|reset>"
    override val description: String = "Set your personal weather."
    override val commandName: String = "pweather"
    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(LiteralArgumentBuilder.literal("day"))
            .then(LiteralArgumentBuilder.literal("sun"))
            .then(LiteralArgumentBuilder.literal("rain"))
            .then(LiteralArgumentBuilder.literal("storm"))
            .then(LiteralArgumentBuilder.literal("reset"))
}