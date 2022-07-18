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

import dev.jaims.moducore.common.const.Times
import dev.jaims.moducore.common.func.getCompactForm
import dev.jaims.moducore.common.func.toTimeFormatted
import kotlin.test.Test
import kotlin.test.assertEquals

class NumbersTest {

    @Test
    fun `test double#getCompactForm()`() {
        assertEquals("0", 0.0.getCompactForm())
        assertEquals("700", 700.0.getCompactForm())
        assertEquals("1k", 1_000.0.getCompactForm())
        assertEquals("-1k", (-1_000.0).getCompactForm())
        assertEquals("1.2m", 1_200_000.0.getCompactForm())
        assertEquals("-1.2m", (-1_200_000.0).getCompactForm())
    }

    @Test
    fun `test int to time formatted`() {
        assertEquals(
            mapOf(
                Times.YEARS to 0,
                Times.MONTHS to 0,
                Times.WEEKS to 0,
                Times.DAYS to 0,
                Times.HOURS to 0,
                Times.MINUTES to 0,
                Times.SECONDS to 1,
            ),
            1.toTimeFormatted()
        )
        assertEquals(
            mapOf(
                Times.YEARS to 0,
                Times.MONTHS to 0,
                Times.WEEKS to 0,
                Times.DAYS to 0,
                Times.HOURS to 0,
                Times.MINUTES to 1,
                Times.SECONDS to 0,
            ),
            60.toTimeFormatted()
        )
        assertEquals(
            mapOf(
                Times.YEARS to 1,
                Times.MONTHS to 1,
                Times.WEEKS to 0,
                Times.DAYS to 2,
                Times.HOURS to 0,
                Times.MINUTES to 0,
                Times.SECONDS to 59,
            ),
            (31536000 + 2592000 + 86400 * 2 + 59).toTimeFormatted()
        )
    }

}