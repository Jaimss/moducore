package dev.jaims.moducore.bukkit.discord.data

import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message

data class ConfigurableMessage(
    val embeded: Boolean = true,
    val content: String? = null,
    val embeds: MutableList<ConfigurableEmbed>
) {

    val asDiscordMessage: Message
        get() = MessageBuilder()
            .setContent(content)
            .apply { if (embeded) setEmbeds(embeds.map { it.asMessageEmbed }) }
            .build()

}
