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
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.perm.Permissions
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.playerNotFound
import me.mattstudios.config.properties.Property
import me.mattstudios.mfgui.gui.guis.Gui
import me.mattstudios.mfgui.gui.guis.GuiItem
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EnderChestCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        val targetName = args.firstOrNull()
        if (targetName != null) {
            if (!Permissions.ENDERCHEST_OTHERS.has(sender)) return
            val target = playerManager.getTargetPlayer(targetName) ?: run {
                sender.playerNotFound(targetName)
                return
            }
            if (Permissions.ENDERCHEST_OTHERS_MODIFY.has(sender, false)) {
                sender.openInventory(target.enderChest)
                return
            }
            val gui = Gui("$targetName's EnderChest (COPY)")
            gui.rows = 3
            gui.addItem(*target.enderChest.storageContents.mapNotNull { if (it == null) it else GuiItem(it) }.toTypedArray())
            gui.setDefaultClickAction { it.isCancelled = true }
            gui.open(sender)
            return
        }
        if (!Permissions.ENDERCHEST.has(sender)) return
        sender.openInventory(sender.enderChest)
    }

    override val module: Property<Boolean> = Modules.COMMAND_ENDERCHEST
    override val usage: String = "/ec [target]"
    override val description: String = "Open your enderchest or another players."
    override val commandName: String = "enderchest"
    override val aliases: List<String> = listOf("ec")
}