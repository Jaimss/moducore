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

package dev.jaims.moducore.bukkit.api.manager

import dev.jaims.moducore.api.manager.PlaytimeManager
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.common.const.Times
import dev.jaims.moducore.common.func.getSecondsDifference
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class DefaultPlaytimeManager(private val plugin: ModuCore) : PlaytimeManager {

    /**
     * Map of join times
     */
    override val joinTimes = mutableMapOf<UUID, Date>()

    /**
     * time since the player has joined in seconds
     */
    override fun getTimeOnlineSinceJoin(uuid: UUID): Int? {
        val joinTime = joinTimes[uuid] ?: return null
        return joinTime.getSecondsDifference(Date())
    }
}

/**
 * A short placeholder for the different [Times].
 */
val Times.shortPlaceholder: String
    get() {
        val timeShortName =
            JavaPlugin.getPlugin(ModuCore::class.java).api.bukkitFileManager.config[Config.TIME_SHORT_NAME]
        return when (this) {
            Times.YEARS -> timeShortName["year"] ?: "yr"
            Times.MONTHS -> timeShortName["month"] ?: "mo"
            Times.WEEKS -> timeShortName["week"] ?: "w"
            Times.DAYS -> timeShortName["day"] ?: "d"
            Times.HOURS -> timeShortName["hour"] ?: "h"
            Times.MINUTES -> timeShortName["minute"] ?: "m"
            Times.SECONDS -> timeShortName["second"] ?: "s"
        }
    }
