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

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.common.message.legacyString
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.miniToComponent
import me.mattstudios.config.properties.Property
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetLoreCommand(override val plugin: ModuCore) : BaseCommand {
    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.SET_LORE.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val loresRaw = args.joinToString(" ").split("\\n")
        val lores = if (Permissions.SET_LORE_FORMAT_AND_COLOR.has(sender, false))
            loresRaw.map { it.miniStyle().miniToComponent() }
        else loresRaw.map(Component::text)

        val item = sender.inventory.itemInMainHand
        if (item.type == Material.AIR) {
            sender.send(Lang.INVALID_ITEM)
            return
        }

        item.meta {
            try {
                lore(lores)
            } catch (ignored: SpigotOnlyNoSuchMethod) {
                plugin.suggestPaperWarning()
                lore = lores.map(Component::legacyString)
            }
        }
        sender.send(Lang.ITEM_MODIFICATION_SUCCESS)
    }

    override val module: Property<Boolean> = Modules.COMMAND_LORE
    override val usage: String = "/setlore <lore with multiple lines>"
    override val description: String = "Set the lore of a item."
    override val commandName: String = "setlore"
}