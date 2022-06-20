package dev.jaims.moducore.bukkit.discord.data

import net.dv8tion.jda.api.entities.MessageEmbed


data class ConfigurableEmbedField(
    val name: String,
    val value: String,
    val inline: Boolean
) {
    val asMessageEmbedField: MessageEmbed.Field
        get() = MessageEmbed.Field(name, value, inline)
}
