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

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface RepairManager {

    /**
     * Method to repair a players item in hand.
     *
     * @param player the player whose item you want to repair
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     */
    fun repair(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Method to repair all things in a players inventory.
     *
     * @param player the player whose item you want to repair
     * @param silent if a target player exists, they will recieve a message if this is true
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     */
    fun repairAll(player: Player, silent: Boolean, executor: CommandSender? = null, sendMessage: Boolean = true)
}