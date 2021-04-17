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

package dev.jaims.moducore.bukkit.command

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.send
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.text.NumberFormat

class NearCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.NEAR.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val nearest = Bukkit.getOnlinePlayers().filter { it.uniqueId != sender.uniqueId }.associateWith { it.location.distance(sender.location) }
            .minByOrNull { it.value }

        if (nearest?.key == null) {
            sender.send(Lang.NEAR_FAILURE)
            return
        }

        sender.send(Lang.NEAR_SUCCESS, nearest.key) {
            it.replace("{distance}", NumberFormat.getInstance().apply {
                this.maximumFractionDigits = 2
            }.format(nearest.value))
        }
    }

    override val module: Property<Boolean> = Modules.COMMAND_NEAR
    override val usage: String = "/near"
    override val description: String = "Find the nearest player to yourself."
    override val commandName: String = "near"
}