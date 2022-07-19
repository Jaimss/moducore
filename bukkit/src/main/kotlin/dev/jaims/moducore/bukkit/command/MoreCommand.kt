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
import dev.jaims.moducore.bukkit.perm.Permissions
import dev.jaims.moducore.bukkit.func.noConsoleCommand
import dev.jaims.moducore.bukkit.func.send
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoreCommand(override val plugin: ModuCore) : BaseCommand {

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.MORE.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val amount = args.firstOrNull()?.toIntOrNull() ?: sender.inventory.itemInMainHand.maxStackSize

        sender.inventory.itemInMainHand.amount = amount
        sender.send(Lang.MORE_SUCCESS) { it.replace("{amount}", amount.toString()) }
    }

    override val module: Property<Boolean> = Modules.COMMAND_MORE
    override val usage: String = "/more <amount>"
    override val description: String = "Give more of an item to yourself. If you specify an amount, it will set the amount to that!"
    override val commandName: String = "more"
}