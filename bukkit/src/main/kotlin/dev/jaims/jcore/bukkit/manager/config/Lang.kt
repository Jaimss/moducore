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

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Lang : SettingsHolder {

    @Comment("{color_name} will be replaced in all strings with the value of the color specified below.")
    @Path("colors")
    val COLORS = Property.create(
        String::class.java, mutableMapOf(
            "gray" to "&8",
            "green" to "&a",
            "red" to "&c",
            "neutral" to "&e",
            "accent" to "&3",
            "name" to "&6"
        )
    )

    @Comment("{prefix_name} will be replaced in all strings with the value of the color specified below")
    @Path("prefix")
    val PREFIXES = Property.create(
        String::class.java, mutableMapOf(
            "good" to "{color_gray}({color_green}!{color_gray}){color_green}",
            "bad" to "{color_gray}({color_red}!{color_gray}){color_red}",
            "neutral" to "{color_gray}({color_neutral}!{color_gray}){color_neutral}",
            "info" to "{color_gray}&l»{color_accent}"
        )
    )

    @Path("no_permission")
    val NO_PERMISSION = Property.create("{prefix_bad} You do not have permission!")

    @Path("no_console_command")
    val NO_CONSOLE_COMMAND = Property.create("{prefix_bad} This command is not intended for console use. Please run as a player.")

    @Path("invalid_number")
    val INVALID_NUMBER = Property.create("{prefix_bad} Invalid number! Using default if one exists.")

    @Path("target_not_found")
    val TARGET_NOT_FOUND = Property.create("{prefix_bad} No player found matching {color_name}{target}.")

    @Path("chat_format")
    val CHAT_FORMAT = Property.create("{color_name}%jcore_displayname% &8&l»&f ")

    @Path("join_message")
    val JOIN_MESSAGE = Property.create("{prefix_good} {color_name}%jcore_displayname% {color_green}has logged in!")

    @Path("quit_message")
    val QUIT_MESSAGE = Property.create("{prefix_bad} {color_name}%jcore_displayname% {color_red}has logged out!")

    // COMMAND MESSAGES START HERE
    @Path("gamemode.changed")
    val GAMEMODE_CHANGED = Property.create("{prefix_good} Your gamemode has been changed to {color_accent}{new}.")

    @Path("gamemode.changed_target")
    val TARGET_GAMEMODE_CHANGED =
        Property.create("{prefix_good} You changed {color_name}%jcore_displayname%'s {color_green}gamemode from {color_accent}{old} {color_green}to {color_accent}{new}.")

    @Path("repair.success")
    val REPAIR_SUCCESS = Property.create("{prefix_good} Your item has been repaired.")

    @Path("repair.success_target")
    val TARGET_REPAIR_SUCCESS =
        Property.create("{prefix_good} You have successfully repaired {color_name}%jcore_displayname%'s {color_green}hand.")

    @Path("repair_all.success")
    val REPAIR_ALL_SUCCESS = Property.create("{prefix_good} All of your items have been repaired!")

    @Path("repair_all.success_target")
    val TARGET_REPAIR_ALL_SUCCESS =
        Property.create("{prefix_good} You repaired all of {color_name}%jcore_displayname%'s {color_green}items.")

    @Path("clear.success")
    val INVENTORY_CLEARED = Property.create("{prefix_good} Your inventory has been cleared.")

    @Path("clear.success_target")
    val TARGET_INVENTORY_CLEARED = Property.create("{prefix_good} You have cleared {color_name}%jcore_displayname%'s {color_green}inventory.")

    @Path("feed.success")
    val FEED_SUCCESS = Property.create("{prefix_good} You have been fed.")

    @Path("feed.success_target")
    val TARGET_FEED_SUCCESS = Property.create("{prefix_good} You have fed {color_name}%jcore_displayname%.")

    @Path("flight.enabled")
    val FLIGHT_ENABLED = Property.create("{prefix_good} Your flight has been {color_accent}enabled!")

    @Path("flight.enabled_target")
    val TARGET_FLIGHT_ENABLED = Property.create("{prefix_good} You have {color_accent}enabled {color_name}%jcore_displayname%'s {color_green}flight!")

    @Path("flight.disabled")
    val FLIGHT_DISABLED = Property.create("{prefix_good} Your flight has been {color_accent}disabled.")

    @Path("flight.disabled_target")
    val TARGET_FLIGHT_DISABLED =
        Property.create("{prefix_good} You have {color_accent}disabled {color_name}%jcore_displayname%'s {color_green}flight.")

    @Path("give.material_not_found")
    val GIVE_MATERIAL_NOT_FOUND = Property.create("{prefix_bad} No material found matching {color_accent}{material}.")

    @Path("give.success")
    val GIVE_SUCCESS = Property.create("{prefix_good} You have been given {color_accent}x{amount} {material}.")

    @Path("give.success_target")
    val TARGET_GIVE_SUCCESS = Property.create("{prefix_good} You have given {color_name}%jcore_displayname% {color_accent}x{amount} {material}.")

    @Path("heal.success")
    val HEAL_SUCCESS = Property.create("{prefix_good} You have been healed!")

    @Path("heal.success_target")
    val TARGET_HEAL_SUCCESS = Property.create("{prefix_good} You have healed {color_name}%jcore_displayname%.")

    @Path("help.header")
    val HELP_HEADER = Property.create("{accent}&lJCore - Help Menu {color_gray}(Filter: {filter})")

    @Path("help.not_found")
    val HELP_NOT_FOUND =
        Property.create("{prefix_bad} No command found matching {color_accent}{name}. {red}Try {color_accent}/help {red} for a list of commands!")

    @Path("help.command_usage")
    val HELP_COMMAND_USAGE = Property.create("{prefix_neutral} {usage}")

    @Path("help.command_description")
    val HELP_COMMAND_DESCRIPTION = Property.create("{prefix_info} {description}")

    @Path("reload.success")
    val RELOAD_SUCCESS = Property.create("{prefix_good} Successfully reloaded JCore! All changes have taken effect.")
}