package dev.jaims.moducore.discord.func

import dev.jaims.moducore.api.manager.StorageManager
import net.dv8tion.jda.api.entities.User

private const val size = 64

/**
 * @return the link of a players head or null if they aren't a linked user
 */
fun User.headLink(storageManager: StorageManager): String? {
    val uuid = storageManager.linkedDiscordAccounts[idLong] ?: return null
    // the UUID can't have dashes
    val uuidString = uuid.toString().replace("-", "")
    return "https://crafthead.net/avatar/${uuidString}/$size"
}

/**
 * @return the link of a players head or null if they aren't a linked user
 */
fun User.cubeLink(storageManager: StorageManager): String? {
    val uuid = storageManager.linkedDiscordAccounts[idLong] ?: return null
    // the UUID can't have dashes
    val uuidString = uuid.toString().replace("-", "")
    return "https://crafthead.net/cube/${uuidString}/$size"
}
