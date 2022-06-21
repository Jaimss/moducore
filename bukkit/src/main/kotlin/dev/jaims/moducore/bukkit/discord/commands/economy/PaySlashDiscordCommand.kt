package dev.jaims.moducore.bukkit.discord.commands.economy

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.discord.commands.SlashDiscordCommand
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class PaySlashDiscordCommand(override val plugin: ModuCore) : SlashDiscordCommand() {
    override fun SlashCommandInteractionEvent.handle() {
        deferReply(true).queue()

        val target = getOption("user")!!.asUser
        val amount = getOption("amount")!!.asDouble

        val storageManager = plugin.api.storageManager

        // the saved player data of this user
        val senderUUID = storageManager.linkedDiscordAccounts[user.idLong] ?: run {
            val message = plugin.api.fileManager.discordLang.userNotLinked.asDiscordMessage()
            hook.sendMessage(message).queue()
            return
        }
        val senderData = runBlocking { storageManager.getPlayerData(senderUUID) }

        // the saved target data of this user
        val targetUUID = storageManager.linkedDiscordAccounts[target.idLong] ?: run {
            val message = plugin.api.fileManager.discordLang.targetNotLinked.asDiscordMessage(
                embedDescriptionModifier = { it.replace("{target}", target.asMention) }
            )
            hook.sendMessage(message).queue()
            return
        }
        val targetData = runBlocking { storageManager.getPlayerData(targetUUID) }

        val economyManager = plugin.api.economyManager

        val hasSufficientFunds = economyManager.hasSufficientFunds(senderUUID, amount)
        if (!hasSufficientFunds) {
            val message = plugin.api.fileManager.discordLang.ecoInvalidFunds.asDiscordMessage(
                embedDescriptionModifier = { it.replace("{amount}", amount.toString()) }
            )
            hook.sendMessage(message).queue()
            return
        }

        economyManager.withdraw(senderUUID, amount)
        economyManager.deposit(targetUUID, amount)

        // TODO success message

    }

    override val description: String = "Pay a user with your in-game balance."
    override val name: String = "pay"
    override val commandData: CommandData = Commands.slash(name, description)
        .addOption(OptionType.USER, "user", "The user you want to pay.", true)
        .addOption(OptionType.NUMBER, "amount", "The amount you want to send.", true)
}