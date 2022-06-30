package dev.jaims.moducore.discord.commands.util.info

import dev.jaims.moducore.api.manager.PlayerManager
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.discord.api.DefaultNameFormatManager
import dev.jaims.moducore.discord.config.DiscordLang
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import java.util.*

/**
 * Return information about a user.
 */
fun getInfoMessage(
    target: User,
    discordLang: DiscordLang,
    storageManager: StorageManager,
    nameFormatManager: DefaultNameFormatManager,
    playerManager: PlayerManager
): Message {

    // if not linked, return unlinked
    val linkedId = storageManager.linkedDiscordAccounts[target.idLong]
        ?: return getUnlinkedInfoMessage(target, discordLang, nameFormatManager)

    // return linked
    return getLinkedInfoMessage(target, linkedId, discordLang, storageManager, nameFormatManager, playerManager)

}

private fun getLinkedInfoMessage(
    target: User,
    linkedUUID: UUID,
    discordLang: DiscordLang,
    storageManager: StorageManager,
    nameFormatManager: DefaultNameFormatManager,
    playerManager: PlayerManager
): Message {
    return discordLang.linkedInfo.asDiscordMessage {
        runBlocking { DefaultNameFormatManager.linkedReplacements(it, target, linkedUUID, playerManager) }
    }
}

private fun getUnlinkedInfoMessage(
    target: User,
    discordLang: DiscordLang,
    nameFormatManager: DefaultNameFormatManager
): Message {
    return discordLang.unlinkedInfo.asDiscordMessage {
        it.replace("{target}", runBlocking { nameFormatManager.getFormatted(target) })
    }
}