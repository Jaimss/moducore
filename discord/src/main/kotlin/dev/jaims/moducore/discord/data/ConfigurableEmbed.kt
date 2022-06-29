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

package dev.jaims.moducore.discord.data

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.bukkit.Bukkit
import java.awt.Color

data class ConfigurableEmbed(
    val title: String? = null,
    val color: String? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val fields: MutableList<ConfigurableEmbedField> = mutableListOf()
) {
    fun asMessageEmbed(
        embedTitleModifier: (String) -> String = { it },
        embedDescriptionModifier: (String) -> String = { it },
        embedFieldNameModifier: (String) -> String = { it },
        embedFieldValueModifier: (String) -> String = { it },
    ): MessageEmbed = EmbedBuilder()
        .setTitle(title?.let { embedTitleModifier(it) })
        .setDescription(description?.let { embedDescriptionModifier(it) })
        .setThumbnail(thumbnailUrl)
        .setColor(
            color?.let {
                Color.getColor(it) ?: try {
                    Color.decode(it)
                } catch (ignored: Throwable) {
                    Bukkit.getLogger().warning("Color: $color is invalid for discord embed colors!")
                    null
                }
            }
        )
        // add fields
        .apply {
            this@ConfigurableEmbed.fields.forEach {
                addField(
                    it.asMessageEmbedField()
                )
            }
        }
        .build()
}
