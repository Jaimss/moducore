package dev.jaims.moducore.api.manager

import dev.jaims.moducore.api.data.Kit
import org.bukkit.inventory.ItemStack

abstract class KitManager {

    protected abstract val kitCache: List<Kit>

    /**
     * Method to get a kit from its [name]
     *
     * @param name the name of the kit
     */
    fun getKit(name: String) = kitCache.firstOrNull { it.name == name }

    /**
     * Create a kit with a [name]
     *
     * @param name the name of the kit
     * @param items the items in the kit
     */
    abstract fun createKit(name: String, items: List<ItemStack>): Kit

}