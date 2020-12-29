/*
 * This file is a part of JCore, licensed under the MIT License.
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

package dev.jaims.jcore.api.manager

import dev.jaims.mcutils.common.Times
import java.util.*

/**
 * A short placeholder for the different [Times].
 */
val Times.shortPlaceholder: String
    get() {
        return when (this) {
            Times.YEARS -> "yr"
            Times.MONTHS -> "mo"
            Times.WEEKS -> "w"
            Times.DAYS -> "d"
            Times.HOURS -> "h"
            Times.MINUTES -> "m"
            Times.SECONDS -> "s"
        }
    }

interface IPlaytimeManager {

    /**
     * A Map of the UUID of a player and the Date they logged in. This is a temporary cache. The players uuid will only
     * be in the map if they are logged in to the server.
     */
    val joinTimes: MutableMap<UUID, Date>

    /**
     * The the time in seconds since they joined the server.
     *
     * @param uuid the players uuid who you want to get
     *
     * @return null if the player is not in the [joinTimes] map, or the time in seconds if they are in the map.
     */
    fun getTimeOnlineSinceJoin(uuid: UUID): Int?

}