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

package dev.jaims.moducore.bukkit.func

import dev.jaims.mcutils.common.getSecondsDifference
import dev.jaims.mcutils.common.toTimeFormatted
import dev.jaims.moducore.bukkit.api.manager.shortPlaceholder
import dev.jaims.moducore.bukkit.config.Config
import me.mattstudios.config.SettingsManager
import java.util.*

lateinit var serverStartTime: Date

/**
 * Get the server uptime as a string
 */
fun getUptimeAsString(config: SettingsManager): String {
    return serverStartTime.getSecondsDifference(Date()).toTimeFormatted().filter { it.value != 0 }
        .map {
            "${config[Config.TIME_NUMBER_COLOR]}${it.value}" +
                    "${config[Config.TIME_ABBREV_COLOR]}${it.key.shortPlaceholder}"
        }
        .joinToString(" ")
}


interface TPSRunnable : Runnable {
    val tps: Double
}

/**
 * Get the TPS string
 */
val tps = object : TPSRunnable {
    private var tickCount = 0L
    private val ticks = arrayOfNulls<Long>(600)

    override fun run() {
        ticks[((tickCount % ticks.size).toInt())] = System.currentTimeMillis()
        tickCount++
    }

    override val tps: Double
        get() = getTps(100)

    fun getTps(getTicks: Int): Double {
        if (tickCount < getTicks) return 20.0
        val target = (tickCount - 1 - getTicks) % ticks.size
        val elapsed = System.currentTimeMillis() - ticks[target.toInt()]!!

        return getTicks / (elapsed / 1000.0)
    }

}