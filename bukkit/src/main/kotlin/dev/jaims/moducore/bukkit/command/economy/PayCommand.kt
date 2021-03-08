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
import dev.jaims.moducore.api.event.economy.ModuCoreEcoPayEvent
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.*
import me.mattstudios.config.properties.Property
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PayCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/pay <target> <amount>"
    override val description: String = "Pay someone some money."
    override val commandName: String = "pay"
    override val module: Property<Boolean> = Modules.ECONOMY

    override val brigadierSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(RequiredArgumentBuilder.argument<String, String>("target", StringArgumentType.word())
                .then(RequiredArgumentBuilder.argument("amount", IntegerArgumentType.integer(0))))

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.PAY.has(sender)) return

        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        if (args.size != 2) {
            sender.usage(usage, description)
            return
        }

        // get arguments
        val target = playerManager.getTargetPlayer(args[0]) ?: run {
            sender.playerNotFound(args[0])
            return
        }
        val amount = args[1].toDoubleOrNull() ?: kotlin.run {
            sender.invalidNumber()
            return
        }

        // do some checks
        if (amount < 0) {
            sender.invalidNumber()
            return
        }
        if (!economyManager.hasSufficientFunds(sender.uniqueId, amount)) {
            sender.send(Lang.INSUFFICIENT_FUNDS)
            return
        }

        economyManager.withdraw(sender.uniqueId, amount)
        economyManager.deposit(target.uniqueId, amount)

        plugin.server.pluginManager.callEvent(ModuCoreEcoPayEvent(target, sender, amount))

        if (!props.isSilent) target.send(Lang.PAID, sender) { it.replace("{amount}", decimalFormat.format(amount)) }
        sender.send(Lang.PAY, target) { it.replace("{amount}", decimalFormat.format(amount)) }
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(playerManager.getPlayerCompletions(args[0]))
            }
        }

    }

}