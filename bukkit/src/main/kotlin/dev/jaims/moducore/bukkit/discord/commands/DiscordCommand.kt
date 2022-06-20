package dev.jaims.moducore.bukkit.discord.commands

import dev.jaims.moducore.bukkit.ModuCore
import net.dv8tion.jda.api.interactions.commands.build.CommandData

interface DiscordCommand {
    val plugin: ModuCore
    val name: String
    val commandData: CommandData
}