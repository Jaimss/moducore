package dev.jaims.moducore.discord.commands.util.info

import dev.jaims.moducore.api.ModuCoreAPI
import dev.jaims.moducore.discord.ModuCoreDiscordBot
import dev.jaims.moducore.discord.commands.DiscordCommand
import dev.jaims.moducore.discord.commands.UserDiscordCommand
import me.mattstudios.config.properties.Property
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

class InfoUserDiscordCommand(override val bot: ModuCoreDiscordBot, override val api: ModuCoreAPI) : UserDiscordCommand() {
    override fun UserContextInteractionEvent.handle() {
        TODO("Not yet implemented")
    }

    override val name: String
        get() = TODO("Not yet implemented")
    override val commandData: CommandData
        get() = TODO("Not yet implemented")
    override val module: Property<Boolean>?
        get() = TODO("Not yet implemented")
}