/*
 * This file is a part of ModuCore, licensed under the MIT License.
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

package dev.jaims.moducore.bukkit.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Lang : SettingsHolder {

    @Comment(
        "{color_whatever} will be replaced in all strings where `whatever: \"blah\"` exists below",
        "This means you can add your own \"css\" variables if you like and have custom color palletes defined."
    )
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

    @Comment(
        "{prefix_name} will be replaced in all strings with the value of the color specified below.",
        "Similar to above you can have custom prefixes defined that you can use anywhere in the Lang File."
    )
    @Path("prefix")
    val PREFIXES = Property.create(
        String::class.java, mutableMapOf(
            "good" to "{color_gray}({color_green}!{color_gray}){color_green}",
            "bad" to "{color_gray}({color_red}!{color_gray}){color_red}",
            "neutral" to "{color_gray}({color_neutral}!{color_gray}){color_neutral}",
            "info" to "{color_gray}&l»{color_accent}"
        )
    )

    @Comment("The default message sent when someone moves before the teleportation timer.")
    @Path("teleportation_cancelled")
    val TELEPORTATION_CANCELLED = Property.create("{prefix_bad} Teleportation cancelled because you moved!")

    @Comment("{permission} will be replaced with the permission node the player needs.")
    @Path("no_permission")
    val NO_PERMISSION = Property.create("{prefix_bad} You do not have permission! &7({permission})")

    @Comment("Sent to console when they cant run the command they are trying to.")
    @Path("no_console_command")
    val NO_CONSOLE_COMMAND =
        Property.create("{prefix_bad} This command is not intended for console use. Please run as a player.")

    @Comment(
        "If a player provides a numerical argument, but it isn't a valid number. For example `/give oak_wood eleven`",
        "instead of `/give oak_wood 11`"
    )
    @Path("invalid_number")
    val INVALID_NUMBER = Property.create("{prefix_bad} Invalid number! Using default if one exists.")

    @Comment("sent to a player if the target of their command is unable to be found.")
    @Path("target_not_found")
    val TARGET_NOT_FOUND = Property.create("{prefix_bad} No player found matching {color_name}{target}.")

    @Comment(
        "Use this to set the chat format. ModuCore is focused more on big picture things, so if you are looking for a much",
        "Bigger chat plugin with more support for different formats, channels, etc, I suggest you disable the chat module and check out another",
        "Chat based plugin"
    )
    @Path("chat_format")
    val CHAT_FORMAT = Property.create("%luckperms_prefix% {color_name}%moducore_displayname% &8&l»&f ")

    @Comment("The message that will be sent when players join the server.")
    @Path("join_message")
    val JOIN_MESSAGE = Property.create("{prefix_good} {color_name}%moducore_displayname% {color_green}has logged in!")

    @Comment("The message that will be sent when players leave the server.")
    @Path("quit_message")
    val QUIT_MESSAGE = Property.create("{prefix_bad} {color_name}%moducore_displayname% {color_red}has logged out!")

    // COMMAND MESSAGES START HERE
    // ECONOMY
    @Path("economy.balance")
    val BALANCE = Property.create("{prefix_good} {color_name}%moducore_displayname%'s {color_green}balance is {color_accent}{balance}.")

    @Path("economy.insufficient_funds")
    val INSUFFICIENT_FUNDS = Property.create("{prefix_bad} Insufficient Funds!")

    @Comment("sent to the person who paid")
    @Path("economy.pay")
    val PAY = Property.create("{prefix_good} Sent {color_accent}{amount} {color_green}to {color_name}%moducore_displayname%.")

    @Comment("sent to the person who recieved")
    @Path("economy.paid")
    val PAID = Property.create("{prefix_good} Recieved {color_accent}{amount} {color_green}from {color_name}%moducore_displayname%.")

    @Path("economy.set")
    val ECONOMY_SET = Property.create("{prefix_good} Set {color_name}%moducore_displayname%'s {color_green}balance to {color_accent}{amount}.")

    @Path("economy.set_target")
    val ECONOMY_SET_TARGET = Property.create("{prefix_good} Your balance has been set to {color_accent}{amount}.")

    @Path("economy.take")
    val ECONOMY_TAKE =
        Property.create("{prefix_good} Took {color_accent}{amount} {color_green}from {color_name}%moducore_displayname%'s {color_green}balance.")

    @Path("economy.take_target")
    val ECONOMY_TAKE_TARGET = Property.create("{prefix_bad} {color_accent}{amount} {color_red}has been taken from your balance.")

    @Path("economy.give")
    val ECONOMY_GIVE = Property.create("{prefix_good} Gave {color_accent}{amount} {color_green}to {color_name}%moducore_displayname%.")

    @Path("economy.give_target")
    val ECONOMY_GIVE_TARGET = Property.create("{prefix_good} {color_accent}{amount} {color_green}has been added to your balance.")

    // GAMEMODE
    @Comment("Sent to the player whose gamemode was changed.")
    @Path("gamemode.changed")
    val GAMEMODE_CHANGED = Property.create("{prefix_good} Your gamemode has been changed to {color_accent}{new}.")

    @Comment("Sent to the sender if the target is not the same as the sender")
    @Path("gamemode.changed_target")
    val TARGET_GAMEMODE_CHANGED =
        Property.create("{prefix_good} You changed {color_name}%moducore_displayname%'s {color_green}gamemode from {color_accent}{old} {color_green}to {color_accent}{new}.")

    // REPAIR
    @Path("repair.success")
    val REPAIR_SUCCESS = Property.create("{prefix_good} Your item has been repaired.")

    @Path("repair.success_target")
    val TARGET_REPAIR_SUCCESS =
        Property.create("{prefix_good} You have successfully repaired {color_name}%moducore_displayname%'s {color_green}hand.")

    @Path("repair_all.success")
    val REPAIR_ALL_SUCCESS = Property.create("{prefix_good} All of your items have been repaired!")

    @Path("repair_all.success_target")
    val TARGET_REPAIR_ALL_SUCCESS =
        Property.create("{prefix_good} You repaired all of {color_name}%moducore_displayname%'s {color_green}items.")

    // SPAWN
    @Path("spawn.set")
    val SPAWN_SET = Property.create("{prefix_good} Set spawn to your current location.")

    @Path("spawn.teleport")
    val SPAWN_TELEPORTING = Property.create("{prefix_good} Teleporting to spawn in {color_accent}{cooldown} seconds...")

    @Path("spawn.teleported")
    val SPAWN_TELEPORTED = Property.create("{prefix_good} Teleported to spawn!")

    @Path("spawn.teleported_target")
    val SPAWN_TELEPORTED_TARGET = Property.create("{prefix_good} Teleported {color_name}%moducore_displayname% {color_green}to spawn.")

    // SPEED
    @Path("speed.fly_success")
    val FLYSPEED_SUCCESS = Property.create("{prefix_good} Your fly speed has been set to {color_accent}{amount}.")

    @Path("speed.fly_success_target")
    val FLYSPEED_SUCCESS_TARGET =
        Property.create("{prefix_good} You set {color_name}%moducore_displayname%'s {color_green}fly speed to {color_accent}{amount}.")

    @Path("speed.walk_success")
    val WALKSPEED_SUCCESS = Property.create("{prefix_good} Your walk speed has been set to {color_accent}{amount}.")

    @Path("speed.walk_success_target")
    val WALKSPEED_SUCCESS_TARGET =
        Property.create("{prefix_good} You set {color_name}%moducore_displayname%'s {color_green}walk speed to {color_accent}{amount}.")

    // TELPORT
    @Comment("Sent to a player when they teleport to someone.")
    @Path("teleport.general.success")
    val TELEPORT_GENERAL_SUCCESS = Property.create("{prefix_good} You teleported to {color_name}%moducore_displayname%.")

    @Comment("Sent to a player when someone teleports to them.")
    @Path("teleport.general.success_target")
    val TELEPORT_GENERAL_SUCCESS_TARGET = Property.create("{prefix_good} {color_name}%moducore_displayname% {color_green}has teleported to you.")

    @Comment("Sent to someone who teleports a player to another player. player is who was teleported, target is who was teleported to")
    @Path("teleport.tp_to_player.success")
    val TELEPORT_P2P_SUCCESS =
        Property.create("{prefix_good} Successfully teleported {color_name}{player} {color_green}to {color_name}{target}.")

    @Comment("Sent to a player when they are teleported to another player.")
    @Path("teleport.tp_to_player.teleport")
    val TELEPORT_P2P_PLAYER = Property.create("{prefix_good} You have been teleported to {color_name}%moducore_displayname%.")

    @Comment("Sent to a player when a player is teleported to them.")
    @Path("teleport.tp_to_player.teleport_to_you")
    val TELEPORT_P2P_TARGET = Property.create("{prefix_good} {color_name}%moducore_displayname% {color_green}has been teleported to you.")

    @Path("teleport.here.success")
    val TELEPORT_HERE_SUCCESS = Property.create("{prefix_good} You teleported {color_name}%moducore_displayname% {color_green}to you.")

    @Path("teleport.here.success_target")
    val TELEPORT_HERE_SUCCESS_TARGET = Property.create("{prefix_good} You have been teleported to {color_name}%moducore_displayname%.")

    @Comment("You can include {world} for the world name.")
    @Path("teleport.position.success")
    val TELEPORT_POSITION_SUCCESS = Property.create("{prefix_good} You have been teleported to {color_accent}{x}, {y}, {z}.")

    @Path("teleport.position.success_target")
    val TELEPORT_POSITION_TARGET =
        Property.create("{prefix_good} You have teleported {color_name}%moducore_displayname% {color_green}to {color_accent}{x}, {y}, {z}.")

    // WARP
    @Path("warp.not_found")
    val WARP_NOT_FOUND = Property.create("{prefix_good} Warp not found with name {color_accent}{name}.")

    @Path("warp.set")
    val WARP_SET = Property.create("{prefix_good} Set warp {color_accent}{name}.")

    @Path("warp.delete")
    val WARP_DELETED = Property.create("{prefix_good} Successfully deleted {name}.")

    @Path("warp.teleporting")
    val WARP_TELEPORTING = Property.create("{prefix_good} Teleporting to {color_accent}{name} {color_green}in {color_accent}{cooldown} seconds.")

    @Path("warp.teleported")
    val WARP_TELEPORTED = Property.create("{prefix_good} Teleported to {color_accent}{name}.")

    @Path("warp.teleported_target")
    val WARP_TELEPORTED_TARGET =
        Property.create("{prefix_good} Teleported {color_name}%moducore_displayname% {color_green} to {color_accent}{name}.")

    // INVENTORY CLEAR
    @Path("clear.success")
    val INVENTORY_CLEARED = Property.create("{prefix_good} Your inventory has been cleared.")

    @Path("clear.success_target")
    val TARGET_INVENTORY_CLEARED =
        Property.create("{prefix_good} You have cleared {color_name}%moducore_displayname%'s {color_green}inventory.")

    // FEED
    @Path("feed.success")
    val FEED_SUCCESS = Property.create("{prefix_good} You have been fed.")

    @Path("feed.success_target")
    val TARGET_FEED_SUCCESS = Property.create("{prefix_good} You have fed {color_name}%moducore_displayname%.")

    // FLY
    @Path("flight.enabled")
    val FLIGHT_ENABLED = Property.create("{prefix_good} Your flight has been {color_accent}enabled!")

    @Path("flight.enabled_target")
    val TARGET_FLIGHT_ENABLED =
        Property.create("{prefix_good} You have {color_accent}enabled {color_name}%moducore_displayname%'s {color_green}flight!")

    @Path("flight.disabled")
    val FLIGHT_DISABLED = Property.create("{prefix_good} Your flight has been {color_accent}disabled.")

    @Path("flight.disabled_target")
    val TARGET_FLIGHT_DISABLED =
        Property.create("{prefix_good} You have {color_accent}disabled {color_name}%moducore_displayname%'s {color_green}flight.")

    // GIVE
    @Path("give.material_not_found")
    val GIVE_MATERIAL_NOT_FOUND = Property.create("{prefix_bad} No material found matching {color_accent}{material}.")

    @Path("give.success")
    val GIVE_SUCCESS = Property.create("{prefix_good} You have been given {color_accent}x{amount} {material}.")

    @Path("give.success_target")
    val TARGET_GIVE_SUCCESS =
        Property.create("{prefix_good} You have given {color_name}%moducore_displayname% {color_accent}x{amount} {material}.")

    // HEAL
    @Path("heal.success")
    val HEAL_SUCCESS = Property.create("{prefix_good} You have been healed!")

    @Path("heal.success_target")
    val TARGET_HEAL_SUCCESS = Property.create("{prefix_good} You have healed {color_name}%moducore_displayname%.")

    // HELP
    @Path("help.header")
    val HELP_HEADER = Property.create("{color_accent}&lModuCore - Help Menu {color_gray}(Filter: {filter})")

    @Path("help.invalid_page")
    val HELP_INVALID_PAGE = Property.create("{prefix_bad} Invalid Page!")

    @Path("help.not_found")
    val HELP_NOT_FOUND =
        Property.create("{prefix_bad} No command found matching {color_accent}{name}. {color_red}Try {color_accent}/help {color_red} for a list of commands!")

    @Path("help.command_usage")
    val HELP_COMMAND_USAGE = Property.create("{prefix_neutral} {usage}")

    @Path("help.command_description")
    val HELP_COMMAND_DESCRIPTION = Property.create("{prefix_info} {description}")

    // HOLOGRAM
    @Path("hologram.not_found")
    val HOLO_NOT_FOUND = Property.create("{prefix_bad} No hologram found with name {color_accent}{name}.")

    @Path("hologram.create.success")
    val HOLO_CREATE_SUCCESS = Property.create("{prefix_good} Successfully created a hologram with name {color_accent}{name}.")

    @Path("hologram.create.failure_two_holograms_with_same_name")
    val HOLO_CREATE_FAIL = Property.create("{prefix_bad} You can not create two holograms with the same name!")

    @Path("hologram.delete.success")
    val HOLO_DELETE = Property.create("{prefix_good} Successfully deleted hologram with name {color_accent}{name}.")

    @Path("hologram.not_looking_at_any_pages")
    val HOLO_NOT_VIEWING_PAGE =
        Property.create("{prefix_bad} You can not perform this operation because you are not viewing any pages of this hologram.")

    @Path("hologram.index_out_of_bounds")
    val INDEX_OUT_OF_BOUNDS =
        Property.create("{prefix_bad} You can not perform this action on the specified index, as it is less than 0 or exceeds the size.")

    @Path("hologram.line.success")
    val HOLO_LINE_MOD_SUCCESS = Property.create("{prefix_good} Successfully modified the lines of this hologram!")

    @Path("hologram.page.switch_success")
    val HOLO_PAGE_SWITCH_SUCCESS = Property.create("{prefix_good} Successfully switched pages for you or your target.")

    @Path("hologram.page.mod_success")
    val HOLO_PAGE_MOD_SUCCESS = Property.create("{prefix_good} Successfully modified the pages of this hologram!")

    // PING
    @Path("ping.your_ping")
    val PING_YOURS = Property.create("{prefix_neutral} Your ping is {color_accent}{ping}ms.")

    @Path("ping.target_ping")
    val PING_TARGET = Property.create("{prefix_neutral} {color_name}%moducore_displayname%'s {color_neutral}ping is {color_accent}{ping}ms.")

    // NICKNAME
    @Comment("A nickname is invalid if it doesn't match the regex set in the config, or it is used by another player.")
    @Path("nickname.invalid")
    val NICKNAME_INVALID = Property.create("{prefix_bad} Invalid nickname! Please try again.")

    @Path("nickname.success")
    val NICKNAME_SUCCESS = Property.create("{prefix_good} Your nickname has been set!")

    @Path("nickname.success_target")
    val NICKNAME_SUCCESS_TARGET =
        Property.create("{prefix_good} Successfully set {color_name}%moducore_displayname%'s {color_green}nickname!")

    @Path("unnick.success")
    val UNNICK_SUCCESS = Property.create("{prefix_good} Your nickname was removed.")

    @Path("unnick.success_target")
    val UNNICK_SUCCESS_TARGET =
        Property.create("{prefix_good} Sucessfully removed {color_name}%moducore_displayname%'s {color_green}nickname!")

    // SUDO
    @Path("sudo")
    val SUDO = Property.create("{prefix_good} Forcing {color_name}%moducore_displayname% {color_green}to run {color_accent}{command}.")

    // RELOAD
    @Path("reload.success")
    val RELOAD_SUCCESS = Property.create("{prefix_good} Successfully reloaded ModuCore! All changes have taken effect.")

    // TIME
    @Path("time.success")
    val TIME_SUCCESS = Property.create("{prefix_good} Successfully set the time to {color_accent}{time}.")

    // WEATHER
    @Path("weather.success")
    val WEATHER_SUCCESS = Property.create("{prefix_good} Successfully set the weather to {color_accent}{weather}.")
}