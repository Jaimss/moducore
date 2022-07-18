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

package dev.jaims.moducore.bukkit.chat

import dev.jaims.moducore.bukkit.const.Permissions
import dev.jaims.moducore.common.message.DEFAULT_URL_PATTERN
import dev.jaims.moducore.common.message.URL_SCHEME_PATTERN
import dev.jaims.moducore.common.message.longHexPattern
import dev.jaims.moducore.common.message.miniStyle
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import org.bukkit.entity.Player

class ChatManager {

    val miniMessage = MiniMessage.builder().tags(TagResolver.empty()).build()

    private val tags = mapOf(
        Permissions.CHAT_TAG.chatTag("color") to StandardTags.color(),
        Permissions.CHAT_TAG.chatTag("reset") to StandardTags.reset(),
        Permissions.CHAT_TAG.chatTag("click") to StandardTags.clickEvent(),
        Permissions.CHAT_TAG.chatTag("hover") to StandardTags.hoverEvent(),
        Permissions.CHAT_TAG.chatTag("keybind") to StandardTags.keybind(),
        Permissions.CHAT_TAG.chatTag("translatable") to StandardTags.translatable(),
        Permissions.CHAT_TAG.chatTag("insertion") to StandardTags.insertion(),
        Permissions.CHAT_TAG.chatTag("rainbow") to StandardTags.rainbow(),
        Permissions.CHAT_TAG.chatTag("gradient") to StandardTags.gradient(),
        Permissions.CHAT_TAG.chatTag("transition") to StandardTags.transition(),
        Permissions.CHAT_TAG.chatTag("font") to StandardTags.font(),
        Permissions.CHAT_TAG.chatTag("newline") to StandardTags.newline(),
        Permissions.CHAT_TAG.chatTag("selector") to StandardTags.selector()
    )

    private val decorations = listOf(
        "bold",
        "italic",
        "underlined",
        "strikethrough",
        "obfuscated"
    ).associate {
        Pair(
            Permissions.CHAT_DECORATION.chatDecoration(it),
            StandardTags.decorations(TextDecoration.valueOf(it.uppercase()))
        )
    }

    // https://github.com/KyoriPowered/adventure/blob/main/4/text-serializer-legacy/src/main/java/net/kyori/adventure/text/serializer/legacy/LegacyComponentSerializerImpl.java#L535
    private val urlReplacementConfig = TextReplacementConfig
        .builder()
        .match(DEFAULT_URL_PATTERN)
        .replacement { url ->
            var clickUrl = url.content()
            if (!URL_SCHEME_PATTERN.matcher(clickUrl).find()) {
                clickUrl = "http://${clickUrl}"
            }
            url.clickEvent(ClickEvent.openUrl(clickUrl))
        }
        .build()

    /**
     * @return a list of allowed tags the [player] can use
     */
    fun getAllowedTags(player: Player) = tags.filter { player.hasPermission(it.key) }.values.toSet()

    /**
     * @return a list of allowed decorations the [player] can use
     */
    fun getAllowedDecorations(player: Player) = decorations.filter { player.hasPermission(it.key) }.values.toSet()

    /**
     * @return the message [Component] that has the approprate allowed tags / decorations
     *
     * @see getAllowedDecorations
     * @see getAllowedTags
     */
    fun getMessage(
        allowedTags: Set<TagResolver>,
        allowedDecorations: Set<TagResolver>,
        allowedURLs: Boolean,
        messageString: String
    ): Component {
        // This could be cached per-person, or per-permisisons if it is slow
        val resolver = TagResolver.builder()
            .apply {
                allowedTags.forEach { resolver(it) }
                allowedDecorations.forEach { resolver(it) }
            }

        val final = miniMessage.deserialize(messageString.miniStyle().longHexPattern(), resolver.build())

        return if (allowedURLs) final.replaceText(urlReplacementConfig)
        else final
    }


}