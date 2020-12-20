package dev.jaims.jcore.bukkit.manager.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Placeholders : SettingsHolder {

    @Comment(
        "Custom Placeholders for PlaceholderAPI. In the example below 'your_custom_placeholder' will be added to 'jcore_' to make a placeholder.",
        "'%jcore_your_custom_placeholder%' will return '%some_other_placeholder%'. This can be used to create 'shorter' placeholders."
    )
    @Path("placeholders")
    val PLACEHOLDERS = Property.create(
        String::class.java,
        mutableMapOf("your_custom_placeholder" to "%some_other_placeholder%")
    )

}