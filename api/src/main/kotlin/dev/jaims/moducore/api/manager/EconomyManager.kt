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