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

package dev.jaims.moducore.bukkit.command.itemmeta

import dev.jaims.mcutils.bukkit.item.meta
import dev.jaims.mcutils.bukkit.util.colorize
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.send
import me.mattstudios.config.properties.Property
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RenameCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.RENAME.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        val nameRaw = args.joinToString(" ")
        val name = if (Permissions.RENAME_FORMAT_AND_COLOR.has(sender, false)) nameRaw.colorize() else nameRaw

        val item = sender.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            sender.send(Lang.INVALID_ITEM)
            return
        }

        item.meta {
            setDisplayName(name)
        }
        sender.send(Lang.ITEM_MODIFICATION_SUCCESS)
    }

    override val module: Property<Boolean> = Modules.COMMAND_RENAME
    override val usage: String = "/rename <name>"
    override val description: String = "Rename an item"
    override val commandName: String = "rename"
    override val aliases: List<String> = listOf("setname")
}