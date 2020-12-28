package dev.jaims.jcore.bukkit.api.economy

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.config.Config
import dev.jaims.jcore.bukkit.util.decimalFormat
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse

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

    override fun hasAccount(playerName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasAccount(playerName: String?, worldName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBalance(playerName: String?): Double {
        TODO("Not yet implemented")
    }

    override fun getBalance(playerName: String?, world: String?): Double {
        TODO("Not yet implemented")
    }

    override fun has(playerName: String?, amount: Double): Boolean {
        TODO("Not yet implemented")
    }

    override fun has(playerName: String?, worldName: String?, amount: Double): Boolean {
        TODO("Not yet implemented")
    }

    override fun withdrawPlayer(playerName: String?, amount: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun withdrawPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun depositPlayer(playerName: String?, amount: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun depositPlayer(playerName: String?, worldName: String?, amount: Double): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun createBank(name: String?, player: String?): EconomyResponse {
        TODO("Not yet implemented")
    }

    override fun createPlayerAccount(playerName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Deletes a bank account with the specified name.
     * @param name of the back to delete
     * @return if the operation completed successfully
     */
    override fun deleteBank(name: String?) =
        EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )

    /**
     * Returns the amount the bank has
     * @param name of the account
     * @return EconomyResponse Object
     */
    override fun bankBalance(name: String?) =
        EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )


    /**
     * Returns true or false whether the bank has the amount specified - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name of the account
     * @param amount to check for
     * @return EconomyResponse Object
     */
    override fun bankHas(name: String?, amount: Double) =
        EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )

    /**
     * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name of the account
     * @param amount to withdraw
     * @return EconomyResponse Object
     */
    override fun bankWithdraw(name: String?, amount: Double) =
        EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )

    /**
     * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name of the account
     * @param amount to deposit
     * @return EconomyResponse Object
     */
    override fun bankDeposit(name: String?, amount: Double) =
        EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )

    override fun isBankOwner(name: String?, playerName: String?) =
        EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )

    override fun isBankMember(name: String?, playerName: String?) =
        EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "JCore does not currently support banks as of version ${plugin.description.version}."
        )

    /**
     * Gets the list of banks
     * @return the List of Banks
     */
    override fun getBanks(): MutableList<String> {
        return mutableListOf()
    }


}