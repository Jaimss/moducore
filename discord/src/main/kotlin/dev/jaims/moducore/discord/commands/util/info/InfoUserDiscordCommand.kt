package dev.jaims.moducore.discord.commands.util.info

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.commands.UserDiscordCommand
import dev.jaims.moducore.discord.config.DiscordBot
import dev.jaims.moducore.discord.config.DiscordModules
import me.mattstudios.config.properties.Property
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class InfoUserDiscordCommand(override val bot: ModuCoreDiscordBot, override val api: ModuCoreAPI) :
    UserDiscordCommand() {
    override fun UserContextInteractionEvent.handle() {
        deferReply(bot.fileManager.discord[DiscordBot.EPHEMERAL_INFO]).queue()

        val message = getInfoMessage(
            target,
            bot.fileManager.discordLang,
            bot.api.storageManager,
            bot.nameFormatManager,
            bot.api.playerManager
        )

        hook.sendMessage(message).queue()
    }

    override val name: String = "User Information"
    override val commandData: CommandData = Commands.user(name)
    override val module: Property<Boolean> = DiscordModules.COMMAND_INFO
}