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

package dev.jaims.moducore.common.test.message

import dev.jaims.moducore.common.message.LEGACY_SECTION
import dev.jaims.moducore.common.message.longHexPattern
import dev.jaims.moducore.common.message.miniStyle
import dev.jaims.moducore.common.message.shortHexPattern
import org.junit.Test
import kotlin.test.assertEquals

class StringsTest {

    @Test
    fun `test that converting from long to short hex works`() {
        assertEquals("&#ff009d", "&#ff009d".shortHexPattern())
        assertEquals("&#FF009d", "&#FF009d".shortHexPattern())
        assertEquals("some before&#FF009d", "some before<#FF009d>".shortHexPattern())
        assertEquals("&#FF009dwith other words", "<#FF009d>with other words".shortHexPattern())
        assertEquals("some before&#FF009dwith other words", "some before<#FF009d>with other words".shortHexPattern())
    }

    @Test
    fun `test that converting from short to long hex works`() {
        assertEquals("<#ff009d>", "&#ff009d".longHexPattern())
        assertEquals("<#FF009d>", "&#FF009d".longHexPattern())
        assertEquals("some before<#FF009d>", "some before&#FF009d".longHexPattern())
        assertEquals("<#FF009d>with other words", "&#FF009dwith other words".longHexPattern())
        assertEquals("some before<#FF009d>with other words", "some before&#FF009dwith other words".longHexPattern())
    }

    @Test
    fun `test string to mini message style`() {
        assertEquals("<dark_blue>This is a <em>mini message", "&1This is a <em>mini message".miniStyle())
        assertEquals("<black><italic><bold>Testing", "&0&o&lTesting".miniStyle())
    }
}