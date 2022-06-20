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

package dev.jaims.moducore.bukkit.discord.config

import dev.jaims.moducore.bukkit.discord.data.ConfigurableEmbed
import dev.jaims.moducore.bukkit.discord.data.ConfigurableMessage
import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object DiscordLang : SettingsHolder {

    @Path("link.code_invalid_or_expired")
    val LINK_CODE_INVALID_OR_EXPIRED = Property.create(
        ConfigurableMessage(
            true,
            null,
            mutableListOf(
                ConfigurableEmbed(
                    "Invalid Code!",
                    "RED",
                    "This **link code** has expired or is invalid. Please try again!"
                )
            )
        )
    )

    @Path("link.success")
    val LINK_SUCCESS = Property.create(
        ConfigurableMessage(
            true,
            null,
            mutableListOf(
                ConfigurableEmbed(
                    "Success",
                    "GREEN",
                    "Successfully linked your account to Minecraft User: `{uuid}`."
                )
            )
        )
    )

}