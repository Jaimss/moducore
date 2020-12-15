/*
 * This file is a part of JCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaims.jcore.bukkit.manager.config

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.mcutils.bukkit.colorize
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

enum class Lang(override val path: String, override val default: Any) : ConfigFileEnum {
    // GENERAL
    GRAY("colors.gray", "&8"),
    GREEN("colors.green", "&a"),
    RED("colors.red", "&c"),
    NEUTRAL("colors.neutral", "&e"),
    PREFIX_GOOD("prefixes.good", "{gray}({green}!{gray}){green}"),
    PREFIX_BAD("prefixes.bad", "{gray}({red}!{gray}){red}"),
    PREFIX_NEUTRAL("prefixes.neutral", "{gray}({neutral}!{gray}){neutral}"),

    // login & logout messages
    JOIN_MESSAGE("join_message", "{prefix_good} {player} has logged in!"),
    QUIT_MESSAGE("login_message", "{prefix_bad} {player} has logged out!"),

    // PERMISSION
    NO_PERMISSION("no_permission", "{prefix_bad} You do not have permission!"),

    // MUST BE PLAYER
    NO_CONSOLE_COMMAND(
        "no_console_command",
        "{prefix_bad} This command is not intended for console use. Please run as a player."
    ),

    // TARGET NOT FOUND
    TARGET_NOT_FOUND("target_not_found", "{prefix_bad} No player found!"),
    TARGET_NOT_FOUND_WITH_NAME("named_target_not_found", "{prefix_bad} No player found matching {neutral}{target}."),

    // FLY
    FLIGHT_ENABLED("fly.enabled", "{prefix_neutral} Your flight has been {neutral}enabled!"),
    FLIGHT_DISABLED("fly.disabled", "{prefix_neutral} Your flight has been {neutral}disabled."),
    FLIGHT_ENABLED_TARGET("fly.target.enabled", "{prefix_neutral} You have {neutral}enabled &a{target}'s flight!"),
    FLIGHT_DISABLED_TARGET("fly.target.disabled", "{prefix_neutral} You have {neutral}disabled &a{target}'s flight.");

    /**
     * Get a message from the enum.
     * Will parse placeholders for a [player] if provided.
     */
    fun get(player: Player? = null): String {
        val m = JavaPlugin.getPlugin(JCore::class.java).managers.fileManager.lang.getString(path) ?: default.toString()
        return m
            .replace("{gray}", GRAY.default.toString())
            .replace("{red}", RED.default.toString())
            .replace("{green}", GREEN.default.toString())
            .replace("{neutral}", NEUTRAL.default.toString())
            .replace("{prefix_good}", PREFIX_GOOD.default.toString())
            .replace("{prefix_bad}", PREFIX_BAD.default.toString())
            .replace("{prefix_neutral}", PREFIX_NEUTRAL.default.toString())
            .colorize(player)
    }

}
