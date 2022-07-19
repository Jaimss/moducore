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

package dev.jaims.moducore.discord.command.util.info

import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.api.manager.player.PlayerManager
import dev.jaims.moducore.discord.api.DefaultNameFormatManager
import dev.jaims.moducore.discord.config.DiscordLang
import dev.jaims.moducore.discord.func.headReplacements
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
    return getLinkedInfoMessage(target, linkedId, discordLang, storageManager, playerManager)

}

private fun getLinkedInfoMessage(
    target: User,
    linkedUUID: UUID,
    discordLang: DiscordLang,
    storageManager: StorageManager,
    playerManager: PlayerManager
): Message {
    return discordLang.linkedInfo.asDiscordMessage {
        DefaultNameFormatManager.linkedReplacements(it, linkedUUID, playerManager)
            .headReplacements(target, storageManager)
    }
}

private fun getUnlinkedInfoMessage(
    target: User,
    discordLang: DiscordLang,
    nameFormatManager: DefaultNameFormatManager
): Message {
    return discordLang.unlinkedInfo.asDiscordMessage {
        it.replace("{target}", nameFormatManager.getFormatted(target))
    }
}