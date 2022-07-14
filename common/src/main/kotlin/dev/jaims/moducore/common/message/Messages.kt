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

package dev.jaims.moducore.common.message

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

val LONG_HEX_PATTERN = "<#[a-fA-F\\d]{6}>".toRegex()
val SHORT_HEX_PATTERN = "&#[a-fA-F\\d]{6}".toRegex()

/**
 * the LEGACY Text Serializer
 */
val LEGACY_SERIALIZER = LegacyComponentSerializer.legacyAmpersand()
    .toBuilder()
    .extractUrls()
    .hexColors()
    .build()

/**
 * the PLAIN Text Serializer
 */
val PLAIN_SERIALIZER = PlainTextComponentSerializer.plainText()

/**
 * The main [MiniMessage] instance
 */
val MINI_MESSAGE = MiniMessage.miniMessage()

/**
 * @return a string colorized for spigot servers by the [LEGACY_SERIALIZER]
 */
inline fun String.legacyToComponent(transform: (String) -> String = { it }) =
    LEGACY_SERIALIZER.deserialize(transform(this.shortHexPattern()))

/**
 * @return a string colorized for spigot servers by the [MINI_MESSAGE]
 */
inline fun String.miniToComponent(transform: (String) -> String = { it }) =
    MINI_MESSAGE.deserialize(transform(this.longHexPattern()))

/**
 * This will return the raw text of a component with ampersand codes, hex codes, etc.
 *
 * @return the raw text of this [Component]
 */
inline fun Component.rawText(transform: (String) -> String = { it }) = transform(LEGACY_SERIALIZER.serialize(this))

/**
 * @return a plain text version of a [Component]
 */
inline fun Component.plainText(transform: (String) -> String = { it }) = transform(PLAIN_SERIALIZER.serialize(this))

/**
 * Convert patterns like <#ff009d> to &#ff009d
 *
 * @return the short hex pattern
 */
fun String.shortHexPattern() = replace(LONG_HEX_PATTERN) {
    it.value.replace("<", "&").replace(">", "")
}

fun String.longHexPattern() = replace(SHORT_HEX_PATTERN) {
    StringBuilder(it.value.replace("&", "<")).append(">").toString()
}