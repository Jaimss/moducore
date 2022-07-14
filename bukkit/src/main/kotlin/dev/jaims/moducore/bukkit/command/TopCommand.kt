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
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.send
import io.papermc.lib.PaperLib
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TopCommand(override val plugin: ModuCore) : BaseCommand {

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.TOP.has(sender)) return

        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val highest = sender.world.getHighestBlockAt(sender.location).location.add(0.5, 1.0, 0.5)
        PaperLib.teleportAsync(sender, highest)
        sender.send(Lang.TOP, sender)
    }

    override val module: Property<Boolean> = Modules.COMMAND_TOP
    override val usage: String = "/top"
    override val description: String = "Teleport to the highest block."
    override val commandName: String = "top"
}