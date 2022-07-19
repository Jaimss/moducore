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
import dev.jaims.moducore.api.event.ModuCoreReloadEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.func.send
import dev.jaims.moducore.bukkit.const.Permissions
import me.mattstudios.config.properties.Property
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ModuCoreReloadCommand(override val plugin: ModuCore) : BaseCommand {

    override val usage: String = "/moducorereload"
    override val description: String = "Reload all files."
    override val commandName: String = "moducorereload"
    override val module: Property<Boolean>? = null

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.RELOAD.has(sender)) return

        // setup the event and check if its cancelled
        val moduCoreReloadEvent = ModuCoreReloadEvent(sender)
        Bukkit.getPluginManager().callEvent(moduCoreReloadEvent)
        if (moduCoreReloadEvent.isCancelled) return

        fileManager.reload()
        kitManager.reload()
        plugin.api.discordFileManager.reload()
        sender.send(Lang.RELOAD_SUCCESS, sender as? Player)
    }
}