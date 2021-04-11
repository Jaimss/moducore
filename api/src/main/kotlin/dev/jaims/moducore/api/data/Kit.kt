package dev.jaims.moducore.api.data

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import org.bukkit.inventory.ItemStack

data class Kit(
    val name: String,
    val items: List<ItemStack>
)

abstract class KitSerializer : JsonSerializer<Kit>, JsonDeserializer<Kit>
