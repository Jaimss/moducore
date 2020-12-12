package dev.jaims.jcore.bukkit.manager.config

import dev.jaims.mcutils.bukkit.colorize
import org.bukkit.entity.Player

enum class Lang(private val message: String) {
    // GENERAL
    GRAY("&8"),
    GREEN("&a"),
    RED("&c"),
    NEUTRAL("&e"),
    PREFIX_GOOD("$GRAY($GREEN!$GRAY)$GREEN"),
    PREFIX_BAD("$GRAY($RED!$GRAY)$RED"),
    PREFIX_NEUTRAL("$GRAY($NEUTRAL!$GRAY)$NEUTRAL"),

    // PERMISSION
    NO_PERMISSION("$PREFIX_BAD You do not have permission!"),

    // MUST BE PLAYER
    NO_CONSOLE_COMMAND("$PREFIX_BAD This command is not intended for console use. Please run as a player."),

    // TARGET NOT FOUND
    TARGET_NOT_FOUND("$PREFIX_BAD No player found!"),
    TARGET_NOT_FOUND_WITH_NAME("$PREFIX_BAD No player found matching ${NEUTRAL}{}."),

    // FLY
    FLIGHT_ENABLED("$PREFIX_NEUTRAL Your flight has been ${NEUTRAL}enabled!"),
    FLIGHT_DISABLED("$PREFIX_NEUTRAL Your flight has been ${NEUTRAL}disabled."),
    FLIGHT_ENABLED_TARGET("$PREFIX_NEUTRAL You have ${NEUTRAL}enabled &a{}'s flight!"),
    FLIGHT_DISABLED_TARGET("$PREFIX_NEUTRAL You have ${NEUTRAL}disabled &a{}'s flight.");

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
        return m.colorize(player)
    }

}
