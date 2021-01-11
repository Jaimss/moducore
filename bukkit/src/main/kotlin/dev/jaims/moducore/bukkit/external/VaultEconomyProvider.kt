package dev.jaims.moducore.bukkit.external

import dev.jaims.mcutils.common.getUUID
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.decimalFormat
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.plugin.ServicePriority

@Suppress("DEPRECATION")
class VaultEconomyProvider(private val plugin: ModuCore) : AbstractEconomy()
{

    private val economyManager = plugin.api.economyManager

    companion object
    {

        val FAILURE = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, null)
        val BANKS_NOT_SUPPORTED =
                EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.")

    }

    fun register()
    {
        if (!plugin.api.fileManager.modules.getProperty(Modules.ECONOMY)) return
        plugin.server.servicesManager.register(Economy::class.java, this, plugin, ServicePriority.Highest)
    }

    /**
     * Checks if economy method is enabled.
     * @return Success or Failure
     */
    override fun isEnabled(): Boolean = true

    /**
     * Gets name of economy method
     * @return Name of Economy Method
     */
    override fun getName(): String = "ModuCore"

    /**
     * Returns true if the given implementation supports banks.
     * @return true if the implementation supports banks
     */
    override fun hasBankSupport(): Boolean = false

    /**
     * Some economy plugins round off after a certain number of digits.
     * This function returns the number of digits the plugin keeps
     * or -1 if no rounding occurs.
     * @return number of digits after the decimal point kept
     */
    override fun fractionalDigits(): Int = -1

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
    override fun currencyNamePlural(): String = plugin.api.fileManager.config.getProperty(Config.CURRENCY_PLURAL)

    /**
     * Returns the name of the currency in singular form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (singular)
     */
    override fun currencyNameSingular(): String = plugin.api.fileManager.config.getProperty(Config.CURRENCY_SINGULAR)

    override fun hasAccount(playerName: String): Boolean
    {
        TODO("Not yet implemented")
    }

    /**
     * Return the balance or -1 if its negative
     */
    override fun getBalance(playerName: String): Double
    {
        val uuid = playerName.getUUID() ?: return -1.0
        return economyManager.getBalance(uuid)
    }


    /**
     * Check if a player has enough money
     */
    override fun has(playerName: String, amount: Double): Boolean
    {
        if (amount < 0) return false
        val uuid = playerName.getUUID() ?: return false
        return economyManager.hasSufficientFunds(uuid, amount)
    }


    override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse
    {
        if (!has(playerName, amount)) return FAILURE
        // get the data
        val uuid = playerName.getUUID() ?: return FAILURE
        economyManager.withdraw(uuid, amount)
        return EconomyResponse(amount, economyManager.getBalance(uuid), EconomyResponse.ResponseType.SUCCESS, null)
    }

    override fun depositPlayer(playerName: String, amount: Double): EconomyResponse
    {
        val uuid = playerName.getUUID() ?: return FAILURE
        economyManager.deposit(uuid, amount)
        return EconomyResponse(amount, economyManager.getBalance(uuid), EconomyResponse.ResponseType.SUCCESS, null)
    }

    /**
     * Create an account, which wont really do much because one is created when a player joins the server no matter what.
     */
    override fun createPlayerAccount(playerName: String): Boolean = false

    /**
     * WORLD SPECIFIC STARTS HERE
     * WORLD SPECIFIC ACCOUNTS AND ECONOMY IS NOT SUPPORTED.
     * IT CALLS THE ECONOMY METHOD FROM ABOVE
     */
    override fun hasAccount(playerName: String, worldName: String?): Boolean = hasAccount(playerName)
    override fun getBalance(playerName: String, world: String?): Double = getBalance(playerName)
    override fun has(playerName: String, worldName: String?, amount: Double): Boolean = has(playerName, amount)
    override fun depositPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse = depositPlayer(playerName, amount)
    override fun createPlayerAccount(playerName: String, worldName: String?): Boolean = createPlayerAccount(playerName)
    override fun withdrawPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse = withdrawPlayer(playerName, amount)

    /**
     * BANKS START HERE
     * BANKS ARE UNSUPORTED BY JCORE
     */
    override fun createBank(name: String?, player: String?): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun deleteBank(name: String?): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun bankBalance(name: String?): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun bankHas(name: String?, amount: Double): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun bankWithdraw(name: String?, amount: Double): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun bankDeposit(name: String?, amount: Double): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun isBankOwner(name: String?, playerName: String?): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun isBankMember(name: String?, playerName: String?): EconomyResponse = BANKS_NOT_SUPPORTED
    override fun getBanks(): MutableList<String> = mutableListOf()

}