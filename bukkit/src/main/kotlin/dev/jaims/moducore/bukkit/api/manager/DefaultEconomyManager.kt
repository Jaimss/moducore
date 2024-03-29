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

import dev.jaims.moducore.api.manager.EconomyManager
import dev.jaims.moducore.api.manager.StorageManager
import dev.jaims.moducore.bukkit.ModuCore
import java.util.*

class DefaultEconomyManager(private val plugin: ModuCore) : EconomyManager {

    private val storageManager: StorageManager by lazy { plugin.api.storageManager }

    /**
     * Get a players balance.
     */
    override fun getBalance(uuid: UUID): Double =
        storageManager.getPlayerData(uuid)?.balance ?: storageManager.loadPlayerData(uuid).join().balance

    /**
     * Set a players balance to a new amount.
     */
    override fun setBalance(uuid: UUID, amount: Double) {
        if (amount < 0) throw IllegalArgumentException("Amount can't be negative!")
        val playerData = storageManager.getPlayerData(uuid) ?: storageManager.loadPlayerData(uuid).join()
        playerData.balance = amount
    }

}