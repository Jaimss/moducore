package dev.jaims.moducore.api.error

import org.bukkit.Bukkit

enum class Errors(val code: String, val description: String) {

    BROADCAST_LIST_EMPTY(
        "broadcast_list_empty",
        "Your broadcast message list is empty but your auto broadcaster is still enabled."
    ),
    INVALID_RTP_WORLD(
        "default_rtp_world_invalid", "The default RTP world in your config.yml (randomtp.default_world)" +
                " is not a valid world. Until this is resolved, the player's world will be used as a fallback."
    ),
    KIT_PLAYER_COMMAND_FAILED(
        "kit_player_command_failed",
        "Failed to execute {} for {} (kit: {})"
    ),
    KIT_CONSOLE_COMMAND_FAILED(
        "kit_console_command_failed",
        "Failed to execute {} for {} (kit: {})"
    ),
    KIT_INVALID_ITEM(
        "kit_invalid_item",
        "An item in the {} kit was not able to be deserialized."
    );

    fun log(vararg replacements: String) {
        var message = "$code: $description\nMore Information: https://github.com/Jaimss/moducore/wiki/Errors#$code"
        replacements.forEach { replacement ->
            message = message.replaceFirst("{}", replacement)
        }
        Bukkit.getLogger().severe(message)
    }


}