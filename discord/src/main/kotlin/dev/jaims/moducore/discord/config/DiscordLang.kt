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

package dev.jaims.moducore.discord.config

import dev.jaims.moducore.discord.data.ConfigurableEmbed
import dev.jaims.moducore.discord.data.ConfigurableEmbedField
import dev.jaims.moducore.discord.data.ConfigurableMessage

data class DiscordLang(
    val linkedUserFormat: String = "{discord_mention} ({minecraft_username})",
    val unlinkedUserFormat: String = "{discord_mention}",
    val linkCodeInvalid: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Invalid Code!",
                "RED",
                "This **link code** has expired or is invalid. Please try again!"
            )
        )
    ),
    val linkSuccess: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Success",
                "GREEN",
                "Successfully linked your account to Minecraft User: `{uuid}`."
            )
        )
    ),
    val userNotLinked: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Error!",
                "RED",
                "You do not have a Minecraft account linked to your discord!\n\n" +
                        "Run `/link` on the Minecraft Server!"
            )
        )
    ),
    val targetNotLinked: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Error!",
                "RED",
                "{target} does not have their Discord linked to their Minecraft!"
            )
        )
    ),
    // economy
    val ecoInvalidFunds: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Error, Invalid Funds!",
                "RED",
                "You do not have {amount} in your `/balance`."
            )
        )
    ),
    val paySuccess: ConfigurableMessage = ConfigurableMessage(
        true,
        "{target}",
        mutableListOf(
            ConfigurableEmbed(
                "Success!",
                "GREEN",
                "{sender} has paid {target} \${amount}"
            )
        )
    ),
    val balance: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Balance",
                "GREEN",
                "Your in-game balance is `\${amount}`!"
            )
        )
    ),
    // info
    val unlinkedInfo: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Information",
                "YELLOW",
                null,
                null,
                mutableListOf(
                    ConfigurableEmbedField("Linked?", "No", false),
                    ConfigurableEmbedField(
                        "Other Info?", "Ask {target} to link their discord account with `/link`!", false
                    )
                )
            )
        )
    ),
    val linkedInfo: ConfigurableMessage = ConfigurableMessage(
        true,
        null,
        mutableListOf(
            ConfigurableEmbed(
                "Information",
                "GREEN",
                null,
                "{cube_url}",
                mutableListOf(
                    ConfigurableEmbedField("Linked?", "Yes!", true),
                    ConfigurableEmbedField("Minecraft Name", "{minecraft_name}", true),
                    ConfigurableEmbedField("Minecraft Nickname", "{minecraft_nickname}", true),
                    ConfigurableEmbedField("Minecraft UUID", "`{minecraft_uuid}`", true),
                )
            )
        )
    )
)