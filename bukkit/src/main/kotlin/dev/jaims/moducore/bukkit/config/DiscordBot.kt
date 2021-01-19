package dev.jaims.moducore.bukkit.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object DiscordBot : SettingsHolder {

    @Path("discord-bot-token")
    val TOKEN = Property.create("your_token_here")

    @Comment("Valid types are `watching`, `playing`, `streaming`, `listening`, `competing`")
    @Path("activity.type")
    val ACTIVITY_TYPE = Property.create("watching")

    @Path("activity.description")
    val ACTIVITY_DESCRIPTION = Property.create("Your Server")

    @Comment("The Stream URL. Only applies if `activity.type` is streaming.")
    @Path("activity.stream_url")
    val ACTIVITY_STREAM_URL = Property.create("https://jaims.dev")

    @Comment("Leave this as blank or an invalid id if you don't want chat sent to discord.")
    @Path("channels.chat_id")
    val CHANNEL_CHAT = Property.create("")

    @Comment("Leave this as blank if you don't want some certain updates sent to your admin discord channel.")
    @Path("channels.admin_id")
    val CHANNEL_ADMIN = Property.create("")

}