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

/**
 * Turns a [String] into a legacy serialized component. [this]'s hex codes
 * turn into the short hex pattern
 *
 * @return a string colorized for spigot servers by the [LEGACY_SERIALIZER]
 */
inline fun String.legacyToComponent(transform: (String) -> String = { it }) =
    LEGACY_SERIALIZER.deserialize(transform(this.shortHexPattern()))

/**
 * Turns a [String] into a mini message component. Turns [this]'s hex codes
 * into the long hex pattern.
 *
 * IMPORTANT: You may want to call [String.miniStyle] before this.
 * @see String.miniStyle
 *
 * @return a string colorized for spigot servers by the [MINI_MESSAGE]
 */
inline fun String.miniToComponent(transform: (String) -> String = { it }) =
    MINI_MESSAGE.deserialize(transform(this.longHexPattern())).replaceText(urlReplacementConfig)

/**
 * Convert patterns like <#...> to &#...
 *
 * @return the short hex pattern of the color code
 */
fun String.shortHexPattern() = replace(LONG_HEX_PATTERN) {
    it.value.replace("<", "&").replace(">", "")
}

/**
 * Convert patterns like &#... to <#...>
 *
 * @return the long hex pattern of the color code
 */
fun String.longHexPattern() = replace(SHORT_HEX_PATTERN) {
    StringBuilder(it.value.replace("&", "<")).append(">").toString()
}

/**
 * Convert legacy codes and formats to minimessage.
 * &1 to <dark_blue>, etc.
 *
 * @see [legacyCodesToMiniStyle]
 */
fun String.miniStyle() = replace(LEGACY_COLOR_CODE_PATTERN) {
    // group 0 is the full code, group 1 is our specific group
    val code = it.groupValues.getOrNull(1) ?: error("Impossible")
    val miniStyle = legacyCodesToMiniStyle[code] ?: error("Also impossible")
    it.value.replace(it.value, miniStyle)
}

fun String.cleanLegacyCodes() = replace(LEGACY_SECTION_CODE_PATTERN) {
    it.value.replace(it.groupValues.first(), "")
}