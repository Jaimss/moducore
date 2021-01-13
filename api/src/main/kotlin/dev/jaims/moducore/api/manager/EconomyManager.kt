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

package dev.jaims.moducore.api.manager

import java.util.*

interface EconomyManager
{

    /**
     * Deposit money into a players account.
     *
     * @param uuid the uuid of the player to deposit to
     * @param amount the amount to deposit
     */
    fun deposit(uuid: UUID, amount: Double)
    {
        if (amount < 0) throw IllegalArgumentException("Amount can't be negative!")
        setBalance(uuid, getBalance(uuid) + amount)
    }

    /**
     * Get a players balance.
     *
     * @param uuid the player to get the balance of
     *
     * @return a [Double] with their balance.
     */
    fun getBalance(uuid: UUID): Double

    /**
     * Check if a player has enough money.
     *
     * @param uuid the player to check
     * @param amount the amount they need to have
     *
     * @return true if they have enough, false if not.
     */
    fun hasSufficientFunds(uuid: UUID, amount: Double): Boolean
    {
        if (amount < 0) throw IllegalArgumentException("Amount can't be negative!")
        return getBalance(uuid) >= amount
    }


    /**
     * Set a players balance to a new amount.
     *
     * @param uuid the player to set
     * @param amount the new amount of their account
     */
    fun setBalance(uuid: UUID, amount: Double)

    /**
     * Withdraw money from a players account.
     *
     * @param uuid the player to withdraw from
     * @param amount the amount to withdraw. Must be non-negative.
     *
     * @throws IllegalArgumentException if the [amount] is negative
     *
     * @return true if it was successful, or false if the player doesnt have enough money.
     */
    fun withdraw(uuid: UUID, amount: Double): Boolean
    {
        if (amount < 0) throw IllegalArgumentException("Amount can't be negative!")
        if (!hasSufficientFunds(uuid, amount)) return false
        setBalance(uuid, getBalance(uuid) - amount)
        return true
    }
}