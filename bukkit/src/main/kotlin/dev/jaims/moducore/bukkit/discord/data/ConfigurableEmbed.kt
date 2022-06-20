package dev.jaims.moducore.bukkit.discord.data

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.bukkit.Bukkit
import java.awt.Color

data class ConfigurableEmbed(
    val title: String?,
    val color: String?,
    val description: String?,
    val fields: MutableList<ConfigurableEmbedField>
) {
    val asMessageEmbed: MessageEmbed
        get() = EmbedBuilder()
            .setTitle(title)
            .setDescription(description)
            .setColor(
                color?.let {
                    Color.getColor(it) ?: try {
                        Color.decode(it)
                    } catch (ignored: Throwable) {
                        Bukkit.getLogger().warning("Color: $color is invalid for discord embed colors!")
                        null
                    }
                }
            )
            // add fields
            .apply { this@ConfigurableEmbed.fields.forEach { addField(it.asMessageEmbedField) } }
            .build()
}
