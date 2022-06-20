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

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.func.tps
import dev.jaims.moducore.bukkit.perm.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender

class TicksPerSecondCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/tps (--no-trim)"
    override val description: String = "Get the Server's ticks per second."
    override val commandName: String = "tps"
    override val module: Property<Boolean> = Modules.COMMAND_TPS

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)

    /**
     * The method to execute a command.
     */
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.TPS.has(sender)) return

        val tps = if (Permissions.TPS_NO_TRIM.has(sender, false) && args.contains("--no-trim")) tps.tps.toString()
        else String.format("%.2f", tps.tps)

        sender.send(Lang.TPS) { it.replace("{tps}", tps) }
    }


}