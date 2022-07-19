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

package dev.jaims.moducore.api.manager.player

import org.bukkit.entity.Player

/**
 * Manages all [Player] related methods.
 */
interface PlayerManager : FlightManager, GameModeManager, HealthManager, NameManager, RepairManager, SpeedManager,
    TeleportationManager {

    /**
     * Get a list of Player Names that can be used in tab completions.
     *
     * @param input the arg that they are currently typing
     *
     * @return A MutableList of Player names as Strings
     */
    fun getPlayerCompletions(input: String): MutableList<String>

    /**
     * Get a target player from their name.
     *
     * @param input the players name
     *
     * @return the [Player] or null, if none is found.
     */
    fun getTargetPlayer(input: String): Player?

}