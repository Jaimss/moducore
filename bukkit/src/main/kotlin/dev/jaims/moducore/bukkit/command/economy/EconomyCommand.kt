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

package dev.jaims.moducore.bukkit.command.economy

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.func.*
import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.common.func.decimalFormat
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class EconomyCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/eco <set|give|take> <amount> <target>"
    override val description: String = "Manage the server's economy."
    override val commandName: String = "economy"
    override val aliases: List<String> = listOf("eco")
    override val module: Property<Boolean> = Modules.ECONOMY

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(LiteralArgumentBuilder.literal<String>("set")
                .then(RequiredArgumentBuilder.argument<String, Int>("amount", IntegerArgumentType.integer())
                    .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word())))).then(
                LiteralArgumentBuilder.literal<String>("give")
                    .then(RequiredArgumentBuilder.argument<String, Int>("amount", IntegerArgumentType.integer())
                        .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word()))))
            .then(LiteralArgumentBuilder.literal<String>("take")
                .then(RequiredArgumentBuilder.argument<String, Int>("amount", IntegerArgumentType.integer())
                    .then(RequiredArgumentBuilder.argument("target", StringArgumentType.word()))))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (args.size != 3) {
            sender.usage(usage, description)
            return
        }
        if (!Permissions.ECONOMY.has(sender)) return

        val target = playerManager.getTargetPlayer(args[2]) ?: run {
            sender.playerNotFound(args[2])
            return
        }

        val amount = args[1].toDoubleOrNull() ?: run {
            sender.invalidNumber()
            return
        }
        if (amount < 0) {
            sender.invalidNumber()
            return
        }

        when (args[0]) {
            "set" -> {
                economyManager.setBalance(target.uniqueId, amount)
                if (!props.isSilent) {
                    target.send(Lang.ECONOMY_SET_TARGET) { it.replace("{amount}", decimalFormat.format(amount)) }
                }
                sender.send(Lang.ECONOMY_SET, target) { it.replace("{amount}", decimalFormat.format(amount)) }
            }
            "give" -> {
                economyManager.deposit(target.uniqueId, amount)
                if (!props.isSilent) {
                    target.send(Lang.ECONOMY_GIVE_TARGET) { it.replace("{amount}", decimalFormat.format(amount)) }
                }
                sender.send(Lang.ECONOMY_GIVE, target) { it.replace("{amount}", decimalFormat.format(amount)) }
            }
            "take" -> {
                if (!economyManager.hasSufficientFunds(target.uniqueId, amount)) {
                    sender.send(Lang.INSUFFICIENT_FUNDS, target)
                    return
                }
                economyManager.withdraw(target.uniqueId, amount)
                if (!props.isSilent) {
                    target.send(Lang.ECONOMY_TAKE_TARGET) { it.replace("{amount}", decimalFormat.format(amount)) }
                }
                sender.send(Lang.ECONOMY_TAKE, target) { it.replace("{amount}", decimalFormat.format(amount)) }
            }
            else -> sender.usage(usage, description)
        }
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(listOf("set", "give", "take").filter { it.startsWith(args[0], ignoreCase = true) })
                3 -> addAll(playerManager.getPlayerCompletions(args[2]))
            }
        }
    }
}