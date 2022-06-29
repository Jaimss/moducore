package dev.jaims.moducore.discord.api

import dev.jaims.mcutils.common.getName
import dev.jaims.moducore.api.manager.NameFormatManager
import dev.jaims.moducore.api.manager.PlayerManager
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.discord.config.DiscordLang
import net.dv8tion.jda.api.entities.User

class DefaultNameFormatManager(
    private val discordLang: DiscordLang,
    private val storageManager: StorageManager,
    private val playerManager: PlayerManager
) : NameFormatManager {

    /**
     * @return the unlinked name format, this only replaces the discord specific things
     */
    private fun getUnlinkedFormat(user: User): String {
        return discordLang.linkedUserFormat
            .replace("{discord_mention}", user.asMention)
            .replace("{discord_tag}", user.asTag)
            .replace("{discord_id}", user.id)
            .replace("{discord_name}", user.name)
            .replace("{discord_discriminator}", user.discriminator)
    }

    override suspend fun getFormatted(user: User): String {

        val unlinkedFormat = getUnlinkedFormat(user)

        val linkedId = storageManager.linkedDiscordAccounts[user.idLong] ?: return unlinkedFormat
        val playerData = storageManager.getPlayerData(linkedId)

        if (playerData.discordID == null) return unlinkedFormat

        return unlinkedFormat
            // minecraft
            .replace("{minecraft_uuid}", linkedId.toString())
            .replace("{minecraft_username}", linkedId.getName() ?: linkedId.toString())
            .replace("{minecraft_displayname}", playerManager.getName(linkedId))
    }

}