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

import java.util.regex.Pattern

/**
 * A [Regex] pattern representing HEX Color Codes like <#...>
 */
val LONG_HEX_PATTERN = "<#[a-fA-F\\d]{6}>".toRegex()

/**
 * A [Regex] pattern representing HEX Color Codes like &#...
 */
val SHORT_HEX_PATTERN = "&#[a-fA-F\\d]{6}".toRegex()

/**
 * The Legacy Section Character
 */
const val LEGACY_SECTION = '§'

/**
 * A [Regex] pattern representing legacy ampersand color and formatting codes like &9 or &a
 */
val LEGACY_COLOR_CODE_PATTERN = "[&${LEGACY_SECTION}]([\\da-fk-or])".toRegex()

// URL PATTERNS: https://github.com/KyoriPowered/adventure/blob/main/4/text-serializer-legacy/src/main/java/net/kyori/adventure/text/serializer/legacy/LegacyComponentSerializerImpl.java
val DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?")!!
val URL_SCHEME_PATTERN = Pattern.compile("^[a-z][a-z0-9+\\-.]*:")!!

/**
 * ALl legacy codes to their mini message style
 */
val legacyCodesToMiniStyle = mapOf(
    "0" to "<black>",
    "1" to "<dark_blue>",
    "2" to "<dark_green>",
    "3" to "<dark_aqua>",
    "4" to "<dark_red>",
    "5" to "<dark_purple>",
    "6" to "<gold>",
    "7" to "<gray>",
    "8" to "<dark_gray>",
    "9" to "<blue>",
    "a" to "<green>",
    "b" to "<aqua>",
    "c" to "<red>",
    "d" to "<light_purple>",
    "e" to "<yellow>",
    "f" to "<white>",
    "k" to "<obfuscated>",
    "l" to "<bold>",
    "m" to "<strikethrough>",
    "n" to "<underlined>",
    "o" to "<italic>",
    "r" to "<reset>"
)
