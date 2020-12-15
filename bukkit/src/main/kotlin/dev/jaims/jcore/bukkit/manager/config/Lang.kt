package dev.jaims.jcore.bukkit.manager.config

import dev.jaims.mcutils.bukkit.colorize
import org.bukkit.entity.Player

enum class Lang(private val message: String) {
    // GENERAL
    GRAY("&8"),
    GREEN("&a"),
    RED("&c"),
    NEUTRAL("&e"),
    PREFIX_GOOD("{gray}({green}!{gray}){green}"),
    PREFIX_BAD("{gray}({red}!{gray}){red}"),
    PREFIX_NEUTRAL("{gray}({neutral}!{gray}){neutral}"),

    // PERMISSION
    NO_PERMISSION("{prefix_bad} You do not have permission!"),

    // MUST BE PLAYER
    NO_CONSOLE_COMMAND("{prefix_bad} This command is not intended for console use. Please run as a player."),

    // TARGET NOT FOUND
    TARGET_NOT_FOUND("{prefix_bad} No player found!"),
    TARGET_NOT_FOUND_WITH_NAME("{prefix_bad} No player found matching {neutral}{}."),

    // FLY
    FLIGHT_ENABLED("{prefix_neutral} Your flight has been {neutral}enabled!"),
    FLIGHT_DISABLED("{prefix_neutral} Your flight has been {neutral}disabled."),
    FLIGHT_ENABLED_TARGET("{prefix_neutral} You have {neutral}enabled &a{}'s flight!"),
    FLIGHT_DISABLED_TARGET("{prefix_neutral} You have {neutral}disabled &a{}'s flight.");

    /**
     * A get method that replaces x amount of `{}` with the [replacements]
     * Usage: Lang.SOMETHING.get("1", "2", "etc")
     *
     * Will parse placeholders for a [player] if provided.
     */
    fun get(vararg replacements: String, player: Player? = null): String {
        var m = message
        replacements.forEach {
            m = m.replaceFirst("{}", it)
        }
        return m
            .replace("{gray}", GRAY.message)
            .replace("{red}", RED.message)
            .replace("{green}", GREEN.message)
            .replace("{neutral}", NEUTRAL.message)
            .replace("{prefix_good}", PREFIX_GOOD.message)
            .replace("{prefix_bad}", PREFIX_BAD.message)
            .replace("{prefix_neutral}", PREFIX_NEUTRAL.message)
            .colorize(player)
    }

}
