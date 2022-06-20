package dev.jaims.moducore.bukkit.discord.commands.link

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.discord.commands.SlashDiscordCommand
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands

class LinkSlashDiscordCommand(override val plugin: ModuCore) : SlashDiscordCommand() {
    override val name: String = "link"
    override val description: String = "Link your Discord and Minecraft Accounts"
    override val commandData: CommandData = Commands.slash(name, description)
        .addOption(OptionType.STRING, "code", "The code that typing /link in Minecraft gives you.")

    override fun SlashCommandInteractionEvent.handle() {
        TODO("Not yet implemented")
    }

}