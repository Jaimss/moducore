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

package dev.jaims.moducore.api.data

/**
 * A data class that hold the relevant player data for each player.
 *
 * @param nickName the players nickname or null if they don't have one
 * @param balance the money the user has
 * @param chatColor the saved chatcolor of the player
 * @param chatPingsEnabled true if the player gets chat pings, false otherwise
 * @param homes the homes they have. the key is the home name, the value is the location.
 * @param kitClaimTimes the map of kitname and system time claimed
 */
data class PlayerData(
    var nickName: String? = null,
    var balance: Double = 0.0,
    var chatColor: String? = null,
    var chatPingsEnabled: Boolean = true,
    val homes: MutableMap<String, LocationHolder> = mutableMapOf(),
    val kitClaimTimes: MutableMap<String, Long> = mutableMapOf(),
)