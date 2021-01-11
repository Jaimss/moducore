package dev.jaims.moducore.bukkit.api.manager

import dev.jaims.moducore.api.manager.EconomyManager
import dev.jaims.moducore.bukkit.ModuCore
import java.util.*

class DefaultEconomyManager(private val plugin: ModuCore) : EconomyManager
{

    val storageManager = plugin.api.storageManager

    /**
     * Get a players balance.
     */
    override fun getBalance(uuid: UUID): Double = storageManager.getPlayerData(uuid).balance

    /**
     * Set a players balance to a new amount.
     */
    override fun setBalance(uuid: UUID, amount: Double)
    {
        if (amount < 0) throw IllegalArgumentException("Amount can't be negative!")
        storageManager.getPlayerData(uuid).balance
    }

}