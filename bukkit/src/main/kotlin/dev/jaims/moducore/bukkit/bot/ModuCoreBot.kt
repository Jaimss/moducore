package dev.jaims.moducore.bukkit.bot

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.DiscordBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

class ModuCoreBot(private val plugin: ModuCore) {

    private val fileManager = plugin.api.fileManager
    lateinit var jda: JDA

    fun enable() {
        jda = JDABuilder.createDefault(fileManager.discord.getProperty(DiscordBot.TOKEN)).apply {
            val activityDesc = fileManager.discord.getProperty(DiscordBot.ACTIVITY_DESCRIPTION)
            when (fileManager.discord.getProperty(DiscordBot.ACTIVITY_TYPE)) {
                "watching" -> setActivity(Activity.watching(activityDesc))
                "playing" -> setActivity(Activity.playing(activityDesc))
                "streaming" -> setActivity(Activity.streaming(activityDesc, fileManager.discord.getProperty(DiscordBot.ACTIVITY_STREAM_URL)))
                "listening" -> setActivity(Activity.listening(activityDesc))
                "competing" -> setActivity(Activity.competing(activityDesc))
            }
        }.build()
    }

}