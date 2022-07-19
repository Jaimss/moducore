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

import dev.jaims.moducore.common.message.legacyToComponent
import dev.jaims.moducore.common.message.miniToComponent
import dev.jaims.moducore.common.message.plainText
import dev.jaims.moducore.common.message.rawText
import org.junit.Test
import kotlin.test.assertEquals

class ComponentsTest {

    @Test
    fun `test that raw text works with mini`() {
        assertEquals(
            "&6&lGold and bold, <gold>With HEX, <#ff0000>and this format",
            "&6&lGold and bold, <#ffaa00>With HEX, &#ff0000and this format".miniToComponent().rawText()
        )
        assertEquals("<gold>This is gold text", "<gold>This is gold text".miniToComponent().rawText())
        assertEquals("<bold><gold>This is gold bold text", "<gold><bold>This is gold bold text"
            .miniToComponent() .rawText())
    }

    @Test
    fun `test that plain text works with mini`() {
        assertEquals(
            "&6&lGold and bold, With HEX, and this format",
            "&6&lGold and bold, <#ffaa00>With HEX, &#ffaa00and this format".miniToComponent().plainText()
        )
        assertEquals("This is gold text", "<gold>This is gold text".miniToComponent().plainText())
        assertEquals("This is gold bold text", "<gold><bold>This is gold bold text".miniToComponent().plainText())
        assertEquals(
            "&6&lGold and bold, With this HEX, and this HEX",
            "<hover:show_text:'Hover Text'>&6&lGold and bold, <#abc123>With this HEX, and &#fd0912this HEX"
                .miniToComponent()
                .plainText()
        )
    }

    @Test
    fun `test that raw text works with legacy`() {
        assertEquals(
            "<bold><gold>Gold and bold, </gold></bold><#abc123>With this HEX, and </#abc123><#fd0912>this HEX",
            "&6&lGold and bold, <#abc123>With this HEX, and &#fd0912this HEX".legacyToComponent().rawText()
        )
    }

    @Test
    fun `test that plain wokrs with legacy`() {
        assertEquals(
            "Gold and bold, With this HEX, and this HEX",
            "&6&lGold and bold, <#abc123>With this HEX, and &#fd0912this HEX".legacyToComponent().plainText()
        )
    }
}