package dev.jaims.moducore.bukkit.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class SlashDiscordCommand : DiscordCommand, ListenerAdapter() {
    abstract val description: String

    abstract fun SlashCommandInteractionEvent.handle()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (name.lowercase() != event.name.lowercase()) return
        event.handle()
    }
}