/*
 * This file is a part of JCore, licensed under the MIT License.
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

package dev.jaims.jcore.bukkit.command

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.manager.config.Config
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.jcore.bukkit.util.noConsoleCommand
import dev.jaims.jcore.bukkit.util.playerNotFound
import dev.jaims.jcore.bukkit.util.usage
import dev.jaims.mcutils.bukkit.send
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveCommand(private val plugin: JCore) : JCoreCommand {
    override val usage: String = "/give <item> [amount] [target]"
    override val description: String = "Give a player a certain amount of an item."
    override val commandName: String = "give"

    private val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        when (args.size) {
            1, 2 -> {
                // Check perms and if they are a player
                // only players can give items to themselves
                if (!Perm.GIVE.has(sender)) return false
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return false
                }
                // get the material and amount
                var amount = 1
                if (args.size == 2) amount = getAmount(sender, args)
                val mat = getMaterial(sender, args[0]) ?: return false
                // add the item and success mesage
                sender.inventory.addItem(ItemStack(mat, amount))
                sender.send(
                    fileManager.getString(Lang.GIVE_SUCCESS, sender as? Player)
                        .replace("{amount}", amount.toString())
                        .replace("{material}", mat.name.toLowerCase())
                )
            }
            3 -> {
                // get permission
                // console can send this as well
                if (!Perm.GIVE_OTHERS.has(sender)) return false
                // get amount, material, *and* target player
                val amount = getAmount(sender, args)
                val mat = getMaterial(sender, args[0]) ?: return false
                val target = playerManager.getTargetPlayer(args[2]) ?: run {
                    sender.playerNotFound(args[0])
                    return false
                }
                // add item and send confirmation message
                target.inventory.addItem(ItemStack(mat, amount))
                if (fileManager.config.getProperty(Config.ALERT_TARGET)) {
                    target.send(
                        fileManager.getString(Lang.GIVE_SUCCESS, target)
                            .replace("{amount}", amount.toString())
                            .replace("{material}", mat.name.toLowerCase())
                    )
                }
                sender.send(
                    fileManager.getString(Lang.TARGET_GIVE_SUCCESS, target)
                        .replace("{target}", playerManager.getName(target.uniqueId))
                        .replace("{amount}", amount.toString())
                        .replace("{material}", mat.name.toLowerCase())
                )
            }
            else -> sender.usage(usage, description)
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val completions = mutableListOf<String>()
        when (args.size) {
            1 -> {
                Material.values().forEach {
                    if (it.name.contains(args[0], ignoreCase = true)) completions.add(it.name.toLowerCase())
                }
            }
            2 -> {
                (1..64).forEach {
                    if (it.toString().contains(args[1], ignoreCase = true)) completions.add(it.toString())
                }
            }
            3 -> completions.addAll(playerManager.getPlayerCompletions(args[2]))
        }
        return completions
    }

    /**
     * @return the amount for the itemstack
     */
    private fun getAmount(sender: CommandSender, args: Array<out String>): Int {
        return args.getOrNull(1)?.toIntOrNull() ?: run {
            sender.send(fileManager.getString(Lang.INVALID_NUMBER, sender as? Player))
            1
        }
    }

    /**
     * @return the [Material] for the itemstack
     */
    private fun getMaterial(sender: CommandSender, name: String): Material? {
        val mat = Material.matchMaterial(name)
        if (mat == null) {
            sender.send(
                fileManager.getString(Lang.GIVE_MATERIAL_NOT_FOUND, sender as? Player)
                    .replace("{material}", name)
            )
        }
        return mat
    }
}