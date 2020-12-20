package dev.jaims.jcore.bukkit.util

import khttp.get

fun getLatestVersion(resourceId: Int): String? {
    val r = get("https://api.spiget.org/v2/resources/$resourceId/versions/latest")
    if (r.statusCode != 200) return null
    val json = r.jsonObject
    return json.getString("name")
}