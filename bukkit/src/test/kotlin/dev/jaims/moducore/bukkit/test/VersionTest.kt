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

package dev.jaims.moducore.bukkit.test

import dev.jaims.moducore.bukkit.util.newerAvailabeVersion
import kotlin.test.Test
import kotlin.test.assertEquals

class VersionTest {

    @Test
    fun `check if the newer version method is working`() {
        assertEquals(newerAvailabeVersion(current = listOf(1, 1, 0), latest = listOf(1, 1, 1)), true)
        assertEquals(newerAvailabeVersion(current = listOf(0, 1, 1), latest = listOf(1, 1, 1)), true)
        assertEquals(newerAvailabeVersion(current = listOf(0, 1, 0), latest = listOf(0, 1, 0)), false)
        assertEquals(newerAvailabeVersion(current = listOf(0, 2, 0), latest = listOf(0, 1, 0)), false)
        assertEquals(newerAvailabeVersion(current = listOf(0, 2, 5), latest = listOf(0, 3, 0)), true)
        assertEquals(newerAvailabeVersion(current = listOf(1, 6, 5), latest = listOf(0, 3, 2)), false)
    }

}