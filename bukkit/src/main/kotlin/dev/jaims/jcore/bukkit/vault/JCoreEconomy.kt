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

package dev.jaims.jcore.bukkit.vault

import dev.jaims.jcore.api.manager.PlayerData
import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.config.Config
import dev.jaims.jcore.bukkit.util.decimalFormat
import dev.jaims.mcutils.common.getUUID
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import net.milkbowl.vault.economy.EconomyResponse.ResponseType.*

class JCoreEconomy(private val plugin: JCore) : AbstractEconomy() {

    /**
     * Checks if economy method is enabled.
     * @return Success or Failure
     */
    override fun isEnabled() = true

    /**
     * Gets name of economy method
     * @return Name of Economy Method
     */
    override fun getName() = "JCore"

    /**
     * Returns true if the given implementation supports banks.
     * @return true if the implementation supports banks
     */
    override fun hasBankSupport() = false

    /**
     * Some economy plugins round off after a certain number of digits.
     * This function returns the number of digits the plugin keeps
     * or -1 if no rounding occurs.
     * @return number of digits after the decimal point kept
     */
    override fun fractionalDigits() = -1

    /**
     * Format amount into a human readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param amount to format
     * @return Human readable string describing amount
     */
    override fun format(amount: Double): String = decimalFormat.format(amount)

    /**
     * Returns the name of the currency in plural form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (plural)
     */
    override fun currencyNamePlural() = plugin.api.fileManager.config.getProperty(Config.CURRENCY_PLURAL)

    /**
     * Returns the name of the currency in singular form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (singular)
     */
    override fun currencyNameSingular() = plugin.api.fileManager.config.getProperty(Config.CURRENCY_SINGULAR)

    /**
     * @return true if the storage file exists for a player, false if not. should always be true
     */
    override fun hasAccount(playerName: String): Boolean {
        val uuid = playerName.getUUID() ?: return false
        return plugin.api.storageManager.getStorageFile(uuid).exists()
    }

    /**
     * Get a players balance based on their name
     */
    override fun getBalance(playerName: String): Double {
        val uuid = playerName.getUUID() ?: return 0.0
        val data = plugin.api.storageManager.getPlayerData(uuid)
        return data.balance
    }

    /**
     * Check if a player has an appropriate amount of money.
     */
    override fun has(playerName: String, amount: Double): Boolean {
        val uuid = playerName.getUUID() ?: return false
        val balance = plugin.api.storageManager.getPlayerData(uuid).balance
        return balance >= amount
    }

    /**
     * Withdraw money from a players account.
     */
    override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse {
        if (amount < 0) return EconomyResponse(0.0, 0.0, FAILURE, null)
        val uuid = playerName.getUUID() ?: return EconomyResponse(0.0, 0.0, FAILURE, null)
        val data = plugin.api.storageManager.getPlayerData(uuid)
        data.balance -= amount
        plugin.api.storageManager.setPlayerData(uuid, data)
        return EconomyResponse(amount, data.balance, SUCCESS, null)
    }

    /**
     * Deposit money to a players account.
     */
    override fun depositPlayer(playerName: String, amount: Double): EconomyResponse {
        if (amount < 0) return EconomyResponse(0.0, 0.0, FAILURE, null)
        val uuid = playerName.getUUID() ?: return EconomyResponse(0.0, 0.0, FAILURE, null)
        val data = plugin.api.storageManager.getPlayerData(uuid)
        data.balance -= amount
        plugin.api.storageManager.setPlayerData(uuid, data)
        return EconomyResponse(amount, data.balance, SUCCESS, null)
    }

    /**
     * Save the default player data to a file for a player.
     */
    override fun createPlayerAccount(playerName: String): Boolean {
        val uuid = playerName.getUUID() ?: return false
        plugin.api.storageManager.setPlayerData(uuid, PlayerData())
        return true
    }

    // banks not supported
    override fun createBank(name: String, player: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun deleteBank(name: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun bankBalance(name: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun bankHas(name: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun bankWithdraw(name: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun bankDeposit(name: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun isBankOwner(name: String, playerName: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun isBankMember(name: String, playerName: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )
    }

    override fun getBanks(): MutableList<String> {
        return mutableListOf()
    }

    // world specific not supported
    override fun createPlayerAccount(playerName: String, worldName: String) = createPlayerAccount(playerName)

    override fun hasAccount(playerName: String, worldName: String) = hasAccount(playerName)

    override fun has(playerName: String, worldName: String, amount: Double) = has(playerName, amount)

    override fun getBalance(playerName: String, world: String) = getBalance(playerName)

    override fun withdrawPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support world specific economy as of version ${plugin.description.version}"
        )
    }

    override fun depositPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            NOT_IMPLEMENTED,
            "JCore does not currently support world specific economy as of version ${plugin.description.version}"
        )
    }

}