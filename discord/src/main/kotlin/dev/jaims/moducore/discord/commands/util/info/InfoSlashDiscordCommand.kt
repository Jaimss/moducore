package dev.jaims.moducore.discord.commands.util.info

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.commands.SlashDiscordCommand
import dev.jaims.moducore.discord.config.DiscordBot
import dev.jaims.moducore.discord.config.DiscordModules
import me.mattstudios.config.properties.Property
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class InfoSlashDiscordCommand(override val bot: ModuCoreDiscordBot, override val api: ModuCoreAPI) :
    SlashDiscordCommand() {

    override fun SlashCommandInteractionEvent.handle() {
        deferReply(bot.fileManager.discord[DiscordBot.EPHEMERAL_INFO]).queue()
        val target = getOption("user")!!.asUser
        val message = getInfoMessage(
            target,
            bot.fileManager.discordLang,
            bot.api.storageManager,
            bot.nameFormatManager,
            bot.api.playerManager
        )

        hook.sendMessage(message).queue()
    }

    override val description: String = "Get information about a user."
    override val name: String = "info"
    override val commandData: CommandData = Commands.slash(name, description)
        .addOption(OptionType.USER, "user", "The user you want info for", true)
    override val module: Property<Boolean> = DiscordModules.COMMAND_INFO
}