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

package dev.jaims.moducore.discord.command.economy

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.command.SlashDiscordCommand
import dev.jaims.moducore.discord.config.DiscordModules
import kotlinx.coroutines.runBlocking
import me.mattstudios.config.properties.Property
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class PaySlashDiscordCommand(
    override val bot: ModuCoreDiscordBot,
    override val api: ModuCoreAPI
) : SlashDiscordCommand(bot, api) {

    override fun SlashCommandInteractionEvent.handle() {
        deferReply(false).queue()

        val target = getOption("user")!!.asUser
        val amount = getOption("amount")!!.asDouble

        val storageManager = api.storageManager

        // the saved player data of this user
        val senderUUID = storageManager.linkedDiscordAccounts[user.idLong] ?: run {
            val message = bot.fileManager.discordLang.userNotLinked.asDiscordMessage()
            hook.sendMessage(message).queue()
            return
        }

        // the saved target data of this user
        val targetUUID = storageManager.linkedDiscordAccounts[target.idLong] ?: run {
            val message = bot.fileManager.discordLang.targetNotLinked
                .asDiscordMessage { it.replace("{target}", target.asMention) }
            hook.sendMessage(message).queue()
            return
        }

        val economyManager = api.economyManager

        val hasSufficientFunds = economyManager.hasSufficientFunds(senderUUID, amount)
        if (!hasSufficientFunds) {
            val message = bot.fileManager.discordLang.ecoInvalidFunds
                .asDiscordMessage { it.replace("{amount}", amount.toString()) }
            hook.sendMessage(message).queue()
            return
        }

        economyManager.withdraw(senderUUID, amount)
        economyManager.deposit(targetUUID, amount)

        hook.sendMessage(bot.fileManager.discordLang.paySuccess
            .asDiscordMessage {
                runBlocking {
                    it.replace("{sender}", bot.nameFormatManager.getFormatted(user))
                        .replace("{target}", bot.nameFormatManager.getFormatted(target))
                        .replace("{amount}", String.format("%.2f", amount))
                }
            }
        ).queue()

    }

    override val description: String = "Pay a user with your in-game balance."
    override val name: String = "pay"
    override val commandData: CommandData = Commands.slash(name, description)
        .addOption(OptionType.USER, "user", "The user you want to pay.", true)
        .addOption(OptionType.NUMBER, "amount", "The amount you want to send.", true)
    override val module: Property<Boolean> = DiscordModules.COMMAND_PAY
}