package dev.jaims.moducore.discord.func

import dev.jaims.moducore.api.manager.StorageManager
import net.dv8tion.jda.api.entities.User

private const val size = 64

/**
 * @return the link of a players head or null if they aren't a linked user
 */
fun User.avatarLink(storageManager: StorageManager): String? {
    val uuid = storageManager.linkedDiscordAccounts[idLong] ?: return null
    return "https://crafthead.net/avatar/$uuid/$size"
}

/**
 * @return the cube of a players head url
 */
fun User.cubeLink(storageManager: StorageManager): String? {
    val uuid = storageManager.linkedDiscordAccounts[idLong] ?: return null
    return "https://crafthead.net/cube/$uuid/$size"
}

/**
 * @return the body of a player url
 */
fun User.bodyLink(storageManager: StorageManager): String? {
    val uuid = storageManager.linkedDiscordAccounts[idLong] ?: return null
    return "https://crafthead.net/body/$uuid/$size"
}

/**
 * @return the bust picture url
 */
fun User.bustLink(storageManager: StorageManager): String? {
    val uuid = storageManager.linkedDiscordAccounts[idLong] ?: return null
    return "https://crafthead.net/bust/$uuid/$size"
}

/**
 * @return the cape picture url
 */
fun User.capeLink(storageManager: StorageManager): String? {
    val uuid = storageManager.linkedDiscordAccounts[idLong] ?: return null
    return "https://crafthead.net/cape/$uuid/$size"
}

