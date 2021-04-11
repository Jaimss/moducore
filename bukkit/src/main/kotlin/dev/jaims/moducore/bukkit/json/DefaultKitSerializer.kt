package dev.jaims.moducore.bukkit.json

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import dev.jaims.moducore.api.data.Kit
import dev.jaims.moducore.api.data.KitSerializer
import dev.jaims.moducore.bukkit.util.ItemStackSerialization
import java.lang.reflect.Type

class DefaultKitSerializer : KitSerializer() {
    override fun serialize(src: Kit, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return Gson().toJsonTree(mapOf(
            "name" to src.name,
            "items" to ItemStackSerialization.itemStackArrayToBase64(src.items.toTypedArray())
        ))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Kit {
        val name = json.asJsonObject.get("name").asString
        val items = ItemStackSerialization.itemStackArrayFromBase64(json.asJsonObject.get("items").asString)
        return Kit(name, items.toList())
    }
}