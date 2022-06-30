package dev.jaims.moducore.discord.commands.economy

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.commands.SlashDiscordCommand
import dev.jaims.moducore.discord.config.DiscordModules
import me.mattstudios.config.properties.Property
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class BalanceSlashDiscordCommand(override val bot: ModuCoreDiscordBot, override val api: ModuCoreAPI) :
    SlashDiscordCommand() {

    override fun SlashCommandInteractionEvent.handle() {
        deferReply(true).queue()

        val linkedUUID = bot.api.storageManager.linkedDiscordAccounts[user.idLong] ?: run {
            val message = bot.fileManager.discordLang.userNotLinked.asDiscordMessage()
            hook.sendMessage(message).queue()
            return
        }

        val balance = bot.api.economyManager.getBalance(linkedUUID)

        val message = bot.fileManager.discordLang.balance.asDiscordMessage {
            it.replace("{amount}", String.format("%.2f", balance))
        }
        hook.sendMessage(message).queue()

    }

    override val description: String = "Check your in-game balance"
    override val name: String = "balance"
    override val commandData: CommandData = Commands.slash(name, description)
    override val module: Property<Boolean> = DiscordModules.COMMAND_BALANCE
}