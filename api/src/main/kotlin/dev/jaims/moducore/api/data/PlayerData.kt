package dev.jaims.moducore.api.data

/**
 * A data class that hold the relevant player data for each player.
 *
 * @param nickName the players nickname or null if they don't have one
 * @param balance the money the user has
 * @param homes the homes they have. the key is the home name, the value is the location.
 */
data class PlayerData(
    var nickName: String? = null,
    var balance: Double = 0.0,
    val homes: MutableMap<String, LocationHolder>
)