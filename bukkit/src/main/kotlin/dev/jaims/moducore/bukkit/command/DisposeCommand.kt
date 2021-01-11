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

import dev.jaims.mcutils.bukkit.log
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.util.Perm
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import javax.print.attribute.standard.Severity

class DisposeCommand(override val plugin: ModuCore) : BaseCommand
{

    override val usage: String = "/dispose"
    override val description: String = "Get rid of your extra items!"
    override val commandName: String = "dispose"

    val fileManager = plugin.api.fileManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {

        if (!Perm.DISPOSE.has(sender)) return

        if (sender !is Player)
        {
            sender.noConsoleCommand()
            return
        }

        val rows = fileManager.config.getProperty(Config.DISPOSE_SIZE)
        if (rows < 1 || rows > 6)
        {
            plugin.log("${Config.DISPOSE_SIZE.path} must be an integer between 1 and 6!", Severity.ERROR)
        }

        val inventory = Bukkit.createInventory(
            null,
            rows * 9,
            fileManager.getString(Config.DISPOSE_TITLE, manager = fileManager.config)
        )
        sender.openInventory(inventory)

        return
    }
}