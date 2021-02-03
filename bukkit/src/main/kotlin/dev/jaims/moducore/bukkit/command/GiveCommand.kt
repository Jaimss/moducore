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

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.*
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveCommand(override val plugin: ModuCore) : BaseCommand {

    override val usage: String = "/give <item> [amount] [target]"
    override val description: String = "Give a player a certain amount of an item."
    override val commandName: String = "give"
    override val aliases: List<String> = listOf("i")

    override val commodoreSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(
                RequiredArgumentBuilder.argument<String, String>("item", StringArgumentType.word())
                    .then(
                        RequiredArgumentBuilder.argument<String, Int>("amount", IntegerArgumentType.integer(0))
                            .then(
                                RequiredArgumentBuilder.argument("target", StringArgumentType.word())
                            )
                    )
            )

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {

        when (args.size) {
            1, 2 -> {
                // Check perms and if they are a player
                // only players can give items to themselves
                if (!Perm.GIVE.has(sender)) return
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return
                }
                // get the material and amount
                var amount = 1
                if (args.size == 2) amount = getAmount(sender, args)
                val mat = getMaterial(sender, args[0]) ?: return
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
                if (!Perm.GIVE_OTHERS.has(sender)) return
                // get amount, material, *and* target player
                val amount = getAmount(sender, args)
                val mat = getMaterial(sender, args[0]) ?: return
                val target = playerManager.getTargetPlayer(args[2]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                // add item and send confirmation message
                target.inventory.addItem(ItemStack(mat, amount))
                if (!props.isSilent) {
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
    private fun getAmount(sender: CommandSender, args: List<String>): Int {
        return args.getOrNull(1)?.toIntOrNull() ?: run {
            sender.invalidNumber()
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