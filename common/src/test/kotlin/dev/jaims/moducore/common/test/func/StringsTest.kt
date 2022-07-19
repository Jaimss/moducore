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

package dev.jaims.moducore.common.test.func

import dev.jaims.moducore.common.func.asLongUUID
import dev.jaims.moducore.common.func.getUUID
import dev.jaims.moducore.common.func.toPastebin
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class StringsTest {

    @Test
    fun `test getting uuid from username`() {
        assertEquals(UUID.fromString("ca606d09-dced-4241-94a6-eaa7d4525d9f"), "Jaimss".getUUID())
    }

    @Test
    fun `test putting a string into a paste service`() {
        // can't predict the key, just assert that it isn't null
        val url = "Anything".toPastebin()
        println("url = $url")
        assertNotNull(url)
    }

    @Test
    fun `test that the short UUID to long UUID conversion works`() {
        assertEquals("ca606d09-dced-4241-94a6-eaa7d4525d9f", "ca606d09dced424194a6eaa7d4525d9f".asLongUUID())
    }

}