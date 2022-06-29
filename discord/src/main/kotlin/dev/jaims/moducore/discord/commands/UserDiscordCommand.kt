package dev.jaims.moducore.discord.commands

import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class UserDiscordCommand : DiscordCommand, ListenerAdapter() {

    abstract fun UserContextInteractionEvent.handle()

    override fun onUserContextInteraction(event: UserContextInteractionEvent) {
        if (name.lowercase() != event.name.lowercase()) return
        event.handle()
    }

}