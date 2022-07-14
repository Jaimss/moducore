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

package dev.jaims.moducore.common.func

import dev.jaims.moducore.common.const.Times
import java.text.DecimalFormat

// a decimal format
val decimalFormat = DecimalFormat("#.##")

fun Double.getCompactForm(): String {
    // edge cases
    if (this == 0.0) return "0"
    if (this < 0) return "-${(-this).getCompactForm()}"
    if (this < 1000) return decimalFormat.format(this)

    var divideBy = 1.0
    var suffix = ""
    for ((n, s) in numberSuffixes) {
        if (this >= n) {
            divideBy = n
            suffix = s
        }
    }

    return decimalFormat.format(this / divideBy) + suffix
}

// a map of prefixes and amounts
private val numberSuffixes = mutableMapOf(
    1_000.0 to "k",
    1_000_000.0 to "m",
    1_000_000_000.0 to "b",
    1_000_000_000_000.0 to "t",
    1_000_000_000_000_000.0 to "q",
    1_000_000_000_000_000_000.0 to "Q",
    1_000_000_000_000_000_000_000.0 to "s",
)

/**
 * Get the different hours, minutes, seconds, etc. from an Integer in seconds
 *
 * @return a map of times and ints where time is the [Times] and [Int] is the amount of that time
 */
fun Int.toTimeFormatted(): Map<Times, Int> {
    var remainder = this
    val years = remainder / 31536000
    remainder -= years * 31536000
    val months = remainder / 2592000
    remainder -= months * 2592000
    val weeks = remainder / 604800
    remainder -= weeks * 604800
    val days = remainder / 86400
    remainder -= days * 86400
    val hours = remainder / 3600
    remainder -= hours * 3600
    val minutes = remainder / 60
    remainder -= minutes * 60
    val seconds = remainder
    return mapOf(
        Times.YEARS to years,
        Times.MONTHS to months,
        Times.WEEKS to weeks,
        Times.DAYS to days,
        Times.HOURS to hours,
        Times.MINUTES to minutes,
        Times.SECONDS to seconds
    )
}