package dev.jaims.jcore.bukkit.util

import java.text.DecimalFormat

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
private val numberSuffixes = mutableMapOf<Double, String>(
    1_000.0 to "k",
    1_000_000.0 to "m",
    1_000_000_000.0 to "b",
    1_000_000_000_000.0 to "t",
    1_000_000_000_000_000.0 to "q",
    1_000_000_000_000_000_000.0 to "Q",
    1_000_000_000_000_000_000_000.0 to "s",
)

// a decimal format
val decimalFormat = DecimalFormat("#.##")
