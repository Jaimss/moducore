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

package dev.jaims.moducore.discord.api

import dev.jaims.mcutils.common.getName
import dev.jaims.moducore.api.manager.NameFormatManager
import dev.jaims.moducore.api.manager.PlayerManager
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.discord.config.DiscordLang
import net.dv8tion.jda.api.entities.User
import java.util.*

class DefaultNameFormatManager(
    private val discordLang: DiscordLang,
    private val storageManager: StorageManager,
    private val playerManager: PlayerManager
) : NameFormatManager {

    companion object {
        fun unlinkedReplacements(string: String, user: User) = string
            .replace("{discord_mention}", user.asMention)
            .replace("{discord_tag}", user.asTag)
            .replace("{discord_id}", user.id)
            .replace("{discord_name}", user.name)
            .replace("{discord_discriminator}", user.discriminator)

        suspend fun linkedReplacements(string: String, linkedId: UUID, playerManager: PlayerManager) =
            string
                .replace("{minecraft_uuid}", linkedId.toString())
                .replace("{minecraft_username}", linkedId.getName() ?: linkedId.toString())
                .replace("{minecraft_nickname}", playerManager.getName(linkedId))

    }

    /**
     * @return the unlinked name format, this only replaces the discord specific things
     */
    private fun getUnlinkedFormat(user: User): String {
        return unlinkedReplacements(discordLang.unlinkedUserFormat, user)
    }

    override suspend fun getFormatted(user: User): String {

        val unlinkedFormat = getUnlinkedFormat(user)

        val linkedId = storageManager.linkedDiscordAccounts[user.idLong] ?: return unlinkedFormat
        val playerData = storageManager.getPlayerData(linkedId)

        if (playerData.discordID == null) return unlinkedFormat

        return unlinkedReplacements(linkedReplacements(discordLang.linkedUserFormat, linkedId, playerManager), user)
    }

}