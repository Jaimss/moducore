/*
 * This file is a part of ModuCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaims.moducore.discord.func

import dev.jaims.moducore.api.manager.StorageManager
import net.dv8tion.jda.api.entities.User

private const val size = 64

fun String.headReplacements(user: User, storageManager: StorageManager) =
    replace("{minecraft_avatar_url}", user.avatarLink(storageManager) ?: "null")
        .replace("{minecraft_cube_url}", user.cubeLink(storageManager) ?: "null")
        .replace("{minecraft_body_url}", user.bodyLink(storageManager) ?: "null")
        .replace("{minecraft_bust_url}", user.bustLink(storageManager) ?: "null")
        .replace("{minecraft_cape_url}", user.capeLink(storageManager) ?: "null")

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

