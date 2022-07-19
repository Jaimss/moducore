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
        "This means you can add your own \"css\" variables if you like and have custom color palletes defined.",
        "SUPPORTS HEX for 1.16+"
    )
    @Path("colors")
    val COLORS = Property.create(
        String::class.java, mutableMapOf(
            "white" to "<white>",
            "light_gray" to "<gray>",
            "gray" to "<dark_gray>",
            "green" to "<green>",
            "red" to "<red>",
            "neutral" to "<yellow>",
            "accent" to "<dark_aqua>",
            "name" to "<gold>"
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
            "info" to "{color_gray}<bold>»{color_accent}"
        )
    )

    @Comment("The default message sent when someone moves before the teleportation timer.")
    @Path("teleportation_cancelled")
    val TELEPORTATION_CANCELLED = Property.create("{prefix_bad} Teleportation cancelled because you moved!")

    @Comment("{permission} will be replaced with the permission node the player needs.")
    @Path("no_permission")
    val NO_PERMISSION = Property.create("{prefix_bad} You do not have permission! {color_light_gray}({permission})")

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

    @Comment("sent if the number needs to be positive, but its not")
    @Path("non_positive_number")
    val NON_POSITIVE_NUMBER = Property.create("{prefix_bad} Invalid number! Must be positive!")

    @Comment("sent to a player if the target of their command is unable to be found.")
    @Path("target_not_found")
    val TARGET_NOT_FOUND = Property.create("{prefix_bad} No player found matching {color_name}{target}.")

    @Comment("The header of the usage message when a command is used wrong.")
    @Path("command_invalid_usage")
    val INVALID_USAGE_HEADER = Property.create("<aqua><bold>ModuCore {color_light_gray}- {color_red}Invalid Usage")

    @Comment(
        "Use this to set the chat format. ModuCore is focused more on big picture things, so if you are looking for a much",
        "Bigger chat plugin with more support for different formats, channels, etc, I suggest you disable the chat module and check out another",
        "Chat based plugin"
    )
    @Path("chat_format")
    val CHAT_FORMAT =
        Property.create(
            "<hover:show_text:'{prefix_neutral} Rank: %luckperms_primary_group_name%<br>" +
                    "{prefix_neutral} Name: %moducore_displayname%<br>" +
                    "{prefix_neutral} Balance: %moducore_balance_formatted%'>" +
                    "%luckperms_prefix% {color_name}%moducore_displayname% {color_gray}<bold>»" +
                    "</hover><reset> "
        )

    @Comment("The message that will be sent when players join the server.")
    @Path("join_message")
    val JOIN_MESSAGE = Property.create("{prefix_good} {color_name}%moducore_displayname% {color_green}has logged in!")

    @Comment("The message that will be sent when players leave the server.")
    @Path("quit_message")
    val QUIT_MESSAGE = Property.create("{prefix_bad} {color_name}%moducore_displayname% {color_red}has logged out!")

    // COMMAND MESSAGES START HERE
    // ECONOMY
    @Path("economy.balance")
    val BALANCE =
        Property.create("{prefix_good} {color_name}%moducore_displayname%'s {color_green}balance is {color_accent}{balance}.")

    @Path("economy.insufficient_funds")
    val INSUFFICIENT_FUNDS = Property.create("{prefix_bad} Insufficient Funds!")

    @Comment("sent to the person who paid")
    @Path("economy.pay")
    val PAY =
        Property.create("{prefix_good} Sent {color_accent}{amount} {color_green}to {color_name}%moducore_displayname%.")

    @Comment("sent to the person who recieved")
    @Path("economy.paid")
    val PAID =
        Property.create("{prefix_good} Recieved {color_accent}{amount} {color_green}from {color_name}%moducore_displayname%.")

    @Path("economy.set")
    val ECONOMY_SET =
        Property.create("{prefix_good} Set {color_name}%moducore_displayname%'s {color_green}balance to {color_accent}{amount}.")

    @Path("economy.set_target")
    val ECONOMY_SET_TARGET = Property.create("{prefix_good} Your balance has been set to {color_accent}{amount}.")

    @Path("economy.take")
    val ECONOMY_TAKE =
        Property.create("{prefix_good} Took {color_accent}{amount} {color_green}from {color_name}%moducore_displayname%'s {color_green}balance.")

    @Path("economy.take_target")
    val ECONOMY_TAKE_TARGET =
        Property.create("{prefix_bad} {color_accent}{amount} {color_red}has been taken from your balance.")

    @Path("economy.give")
    val ECONOMY_GIVE =
        Property.create("{prefix_good} Gave {color_accent}{amount} {color_green}to {color_name}%moducore_displayname%.")

    @Path("economy.give_target")
    val ECONOMY_GIVE_TARGET =
        Property.create("{prefix_good} {color_accent}{amount} {color_green}has been added to your balance.")

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
    val SPAWN_TELEPORTED_TARGET =
        Property.create("{prefix_good} Teleported {color_name}%moducore_displayname% {color_green}to spawn.")

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
    val TELEPORT_GENERAL_SUCCESS =
        Property.create("{prefix_good} You teleported to {color_name}%moducore_displayname%.")

    @Comment("Sent to a player when someone teleports to them.")
    @Path("teleport.general.success_target")
    val TELEPORT_GENERAL_SUCCESS_TARGET =
        Property.create("{prefix_good} {color_name}%moducore_displayname% {color_green}has teleported to you.")

    @Comment("Sent to someone who teleports a player to another player. player is who was teleported, target is who was teleported to")
    @Path("teleport.tp_to_player.success")
    val TELEPORT_P2P_SUCCESS =
        Property.create("{prefix_good} Successfully teleported {color_name}{player} {color_green}to {color_name}{target}.")

    @Comment("Sent to a player when they are teleported to another player.")
    @Path("teleport.tp_to_player.teleport")
    val TELEPORT_P2P_PLAYER =
        Property.create("{prefix_good} You have been teleported to {color_name}%moducore_displayname%.")

    @Comment("Sent to a player when a player is teleported to them.")
    @Path("teleport.tp_to_player.teleport_to_you")
    val TELEPORT_P2P_TARGET =
        Property.create("{prefix_good} {color_name}%moducore_displayname% {color_green}has been teleported to you.")

    @Path("teleport.here.success")
    val TELEPORT_HERE_SUCCESS =
        Property.create("{prefix_good} You teleported {color_name}%moducore_displayname% {color_green}to you.")

    @Path("teleport.here.success_target")
    val TELEPORT_HERE_SUCCESS_TARGET =
        Property.create("{prefix_good} You have been teleported to {color_name}%moducore_displayname%.")

    @Comment("You can include {world} for the world name.")
    @Path("teleport.position.success")
    val TELEPORT_POSITION_SUCCESS =
        Property.create("{prefix_good} You have been teleported to {color_accent}{x}, {y}, {z}.")

    @Path("teleport.position.success_target")
    val TELEPORT_POSITION_TARGET =
        Property.create("{prefix_good} You have teleported {color_name}%moducore_displayname% {color_green}to {color_accent}{x}, {y}, {z}.")

    @Path("teleport.tpr.already_sent_to_player")
    val TPR_ALREADY_SENT_TO_PLAYER = Property.create("{prefix_bad} You already sent a request to this player!")

    @Path("teleport.tpr.no_pending_requests")
    val TPR_NO_PENDING_REQUESTS = Property.create("{prefix_bad} You do not have any pending teleport requests!")

    @Path("teleport.tpr.request_accepted")
    val TPR_REQUEST_ACCEPTED =
        Property.create("{prefix_good} Your teleport request to %moducore_displayname% has been accepted, teleporting in {color_accent}{cooldown} {color_green}seconds...")

    @Path("teleport.tpr.request_accepted_target")
    val TPR_REQUEST_ACCEPTED_TARGET =
        Property.create("{prefix_good} Successfully accepted {color_name}%moducore_displayname%'s {color_green}teleport request!")

    @Path("teleport.tpr.request_cancelled")
    val TPR_REQUEST_CANCELLED =
        Property.create("{prefix_good} Successfully cancelled your request to {color_name}%moducore_displayname%.")

    @Path("teleport.tpr.request_denied")
    val TPR_REQUEST_DENIED =
        Property.create("{prefix_bad} Your teleport request to %moducore_displayname% has been denied.")

    @Path("teleport.tpr.request_denied_target")
    val TPR_REQUEST_DENIED_TARGET =
        Property.create("{prefix_good} Successfully denied %moducore_displayname%'s request!")

    @Path("teleport.tpr.request_sent")
    val TPR_TELEPORT_REQUEST_SENT = Property.create(
        "<hover:show_text:'{color_green}Click to Cancel'>" +
                "<click:suggest_command:/tpcancel>" +
                "{prefix_good}Sent a teleport request to {color_name}%moducore_displayname%. " +
                "{color_green}Type {color_accent}/tpcancel {color_green}or click here to cancel!"
    )

    @Path("teleport.tpr.request_received")
    val TPR_REQUEST_RECEIVED = Property.create(
        "{prefix_good} {color_name}%moducore_displayname% {color_green}is requesting to teleport to you." +
                "Type (or click)<click:suggest_command:/tpaccept>" +
                "{color_accent}/tpaccept" +
                "</click> {color_green}to accept or" +
                "<click:suggest_command:/tpdeny>" +
                "{color_accent}/tpdeny" +
                "</click> {color_green}to deny."
    )

    // WARP
    @Path("warp.not_found")
    val WARP_NOT_FOUND = Property.create("{prefix_good} Warp not found with name {color_accent}{name}.")

    @Path("warp.set")
    val WARP_SET = Property.create("{prefix_good} Set warp {color_accent}{name}.")

    @Path("warp.delete")
    val WARP_DELETED = Property.create("{prefix_good} Successfully deleted {name}.")

    @Path("warp.teleporting")
    val WARP_TELEPORTING =
        Property.create("{prefix_good} Teleporting to {color_accent}{name} {color_green}in {color_accent}{cooldown} seconds.")

    @Path("warp.teleported")
    val WARP_TELEPORTED = Property.create("{prefix_good} Teleported to {color_accent}{name}.")

    @Path("warp.teleported_target")
    val WARP_TELEPORTED_TARGET =
        Property.create("{prefix_good} Teleported {color_name}%moducore_displayname% {color_green} to {color_accent}{name}.")

    // CHATCOLOR
    @Path("chatcolor.success")
    val CHATCOLOR_SUCCESS = Property.create("{prefix_good} Successfully set your chat color to {color}this!")

    @Path("chatcolor.removed")
    val CHATCOLOR_REMOVED = Property.create("{prefix_good} Successfully removed your chatcolor!")

    @Path("chatcolor.custom_prompt")
    val CHATCOLOR_PROMPT = Property.create("{prefix_neutral} Your next message will be your chatcolor...")

    @Path("chatping.self.enabled")
    val CHATPING_ENABLED = Property.create("{prefix_neutral} Set your chat pings to {color_green}enabled.")

    @Path("chatping.self.disabled")
    val CHATPING_DISABLED = Property.create("{prefix_neutral} Set your chat pings to {color_red}disabled.")

    @Path("chatping.target.enabled")
    val CHATPING_ENABLED_TARGET =
        Property.create("{prefix_neutral} Set %moducore_displayname%'s chat pings to {color_green}enabled.")

    @Path("chatping.target.disabled")
    val CHATPING_DISABLED_TARGET =
        Property.create("{prefix_neutral} Set %moducore_displayname%'s chat pings to {color_red}disabled.")

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

    @Path("hat.remove_helmet")
    val HAT_REMOVE_HELMET = Property.create("{prefix_bad} Please remove your helmet before setting your hat!")

    @Path("hat.success")
    val HAT_SUCCESS = Property.create("{prefix_good} Successfully set your hat to this item!")

    // HEAL
    @Path("heal.success")
    val HEAL_SUCCESS = Property.create("{prefix_good} You have been healed!")

    @Path("heal.success_target")
    val TARGET_HEAL_SUCCESS = Property.create("{prefix_good} You have healed {color_name}%moducore_displayname%.")

    // HELP
    @Path("help.header")
    val HELP_HEADER = Property.create("{color_accent}<bold>ModuCore - Help Menu {color_gray}(Filter: {filter})")

    @Path("help.invalid_page")
    val HELP_INVALID_PAGE = Property.create("{prefix_bad} Invalid Page!")

    @Path("help.not_found")
    val HELP_NOT_FOUND =
        Property.create("{prefix_bad} No command found matching {color_accent}{name}. {color_red}Try {color_accent}/help {color_red} for a list of commands!")

    @Comment("{description} is replaced with the format from below")
    @Path("help.command_usage")
    val HELP_COMMAND_USAGE = Property.create("<hover:show_text:'{color_neutral}{description}'>{prefix_neutral} {usage}")

    // HOLOGRAM
    @Comment("Sent when you teleport a hologram to your location.")
    @Path("hologram.tphere")
    val HOLO_TPHERE = Property.create("{prefix_good} Teleported the hologram to your location!")

    @Comment("Sent to the player when they request the hologram info")
    @Path("hologram.info.header")
    val HOLOGRAM_INFO_HEADER = Property.create(
        "<hover:show_text:'{color_accent}Name: {name}<br>{color_accent}Pages: {pages}'>Hologram Info:"
    )

    @Path("hologram.info.page_title_format")
    val HOLOGRAM_PAGE_FORMAT = Property.create(
        "<hover:show_text:'{color_accent}Lines: {lines}'>Page {index}:"
    )

    @Path("hologram.info.line_format")
    val HOLOGRAM_INFO_LINES_FORMAT = Property.create(
        "<click:suggest_command:/holo setline {name} {index} {line}>{color_accent}({index}) {color_gray}- {line}"
    )

    @Comment("Sent when the hologram name you specify is unable to be found.")
    @Path("hologram.not_found")
    val HOLO_NOT_FOUND = Property.create("{prefix_bad} No hologram found with name {color_accent}{name}.")

    @Comment("Sent when a hologram is successfully created.")
    @Path("hologram.create.success")
    val HOLO_CREATE_SUCCESS =
        Property.create("{prefix_good} Successfully created a hologram with name {color_accent}{name}.")

    @Comment("Sent if you try to create a hologram that already exists.")
    @Path("hologram.create.failure_two_holograms_with_same_name")
    val HOLO_CREATE_FAIL = Property.create("{prefix_bad} You can not create two holograms with the same name!")

    @Comment("Sent if you successfully delete a hologram.")
    @Path("hologram.delete.success")
    val HOLO_DELETE = Property.create("{prefix_good} Successfully deleted hologram with name {color_accent}{name}.")

    @Comment(
        "Sent if you are not viewing the hologram when you try to modify it",
        "This is a thing because modifying the lines of a hologram modifies the page you are looking at, so if you aren't",
        "looking at a page, it won't work!"
    )
    @Path("hologram.not_looking_at_any_pages")
    val HOLO_NOT_VIEWING_PAGE =
        Property.create("{prefix_bad} You can not perform this operation because you are not viewing any pages of this hologram.")

    @Comment("Sent if the line/page index is too big or too small relative to the lines/pages size.")
    @Path("hologram.index_out_of_bounds")
    val INDEX_OUT_OF_BOUNDS =
        Property.create("{prefix_bad} You can not perform this action on the specified index, as it is less than 0 or exceeds the size.")

    @Path("hologram.line.success")
    val HOLO_LINE_MOD_SUCCESS = Property.create("{prefix_good} Successfully modified the lines of this hologram!")

    @Path("hologram.page.switch_success")
    val HOLO_PAGE_SWITCH_SUCCESS = Property.create("{prefix_good} Successfully switched pages for you or your target.")

    @Path("hologram.page.mod_success")
    val HOLO_PAGE_MOD_SUCCESS = Property.create("{prefix_good} Successfully modified the pages of this hologram!")

    // HOME
    @Path("home.not_found")
    val HOME_NOT_FOUND = Property.create(
        "{prefix_bad} No home found by the name {color_accent}{name}. {color_gray}(Names are case-sensitive.)"
    )

    @Path("home.teleporting")
    val HOME_TELEPORTING = Property.create(
        "{prefix_good} Teleporting to your home {color_gray}({name}) {color_green}in {color_accent}{cooldown} seconds..."
    )

    @Path("home.set_success")
    val HOME_SET_SUCCESS =
        Property.create(
            "{prefix_good} Successfully set a home named {color_accent}{name}! {color_green}If this was a mistake type " +
                    "{color_accent}undo {color_green}in chat in the next {color_accent}{time}s {color_green}to cancel it!"
        )

    @Path("home.set_failure")
    val HOME_SET_FAILURE = Property.create("{prefix_bad} You already have too many homes.")

    @Path("home.homes")
    val HOMES = Property.create("{prefix_neutral} {color_name}%moducore_displayname%'s {color_neutral}Homes: {homes}")

    @Path("home.delhome_success")
    val DELHOME_SUCCESS = Property.create("{prefix_good} Successfully deleted a home named {color_accent}{name}.")

    @Path("home.home_success")
    val HOME_SUCCESS = Property.create("{prefix_good} Successfully teleported to home with name {color_accent}{name}!")

    @Path("home.home_success_target")
    val HOME_SUCCESS_TARGET =
        Property.create("{prefix_good} Successfully teleported to {color_name}%moducore_displayname%'s {color_green}home with name {color_accent}{name}.")

    @Path("home.delhome_success_target")
    val DELHOME_SUCCESS_TARGET =
        Property.create("{prefix_good} Successfully deleted {color_name}%moducore_displayname%'s {color_green}home named {color_accent}{name}.")

    @Path("home.set_undone")
    val HOME_SET_UNDONE = Property.create("{prefix_good} Successfully undid the home creation.")

    // ITEM META
    @Path("invalid_item")
    val INVALID_ITEM = Property.create("{prefix_bad} Please hold a different item in your hand!")

    @Path("item_modification_success")
    val ITEM_MODIFICATION_SUCCESS = Property.create("{prefix_good} Successfully modified your item!")

    // KITS
    @Path("kits.available")
    val KITS_AVAILABLE = Property.create("{prefix_neutral} Available Kits: {kits}")

    @Path("kits.already_exists")
    val KIT_ALREADY_EXISTS = Property.create("{prefix_bad} A kit with this name already exists!")

    @Comment("{amount} for the items amoumt, {cooldown} for the cooldown length, {name} for the kit name")
    @Path("kits.created")
    val KIT_CREATED =
        Property.create("{prefix_good} Successfully created a kit named {color_accent}{name} {color_green}with a {color_accent}{cooldown} cooldown.")

    @Path("kits.deleted")
    val KIT_DELETED = Property.create("{prefix_good} Successfully deleted a kit named {color_accent}{name}.")

    @Path("kits.cooldown")
    val KIT_COOLDOWN = Property.create("{prefix_bad} You are still on cooldown for this kit for {color_accent}{time}!")

    @Path("kits.claimed")
    val KIT_CLAIMED = Property.create("{prefix_good} Successfully claimed {color_accent}{name}!")

    @Path("kits.claimed_target")
    val KIT_CLAIMED_TARGET =
        Property.create("{prefix_good} Successfully claimed {color_accent}{name} {color_green}for {color_name}%moducore_displayname%.")

    @Path("kits.not_found")
    val KIT_NOT_FOUND = Property.create("{prefix_bad} No kit found named {color_accent}{name}!")

    // LOCKDOWN
    @Path("lockdown.set")
    val LOCKDOWN_SET =
        Property.create("{prefix_good} Successfully locked down the server to users with the {color_accent}moducore.lockdown.join.{group} {color_green}permission.")

    @Path("lockdown.removed")
    val LOCKDOWN_REMOVED = Property.create("{prefix_good} Successfully removed the lockdown.")

    @Comment("{group} will be replaced with the group name")
    @Path("lockdown.cant_join")
    val LOCKDOWN_CANT_JOIN = Property.create("{prefix_bad} This server is currently locked! Please join later!")

    @Path("lockdown.status.locked")
    val LOCKDOWN_STATUS_LOCKED =
        Property.create("{prefix_neutral} This server is currently locked with group: {color_accent}{group}.")

    @Path("lockdown.status.unlocked")
    val LOCKDOWN_STATUS_UNLOCKED =
        Property.create("{prefix_neutral} This server is currently unlocked. Anyone can join!")

    // PING
    @Path("ping.your_ping")
    val PING_YOURS = Property.create("{prefix_neutral} Your ping is {color_accent}{ping}ms.")

    @Path("ping.target_ping")
    val PING_TARGET =
        Property.create("{prefix_neutral} {color_name}%moducore_displayname%'s {color_neutral}ping is {color_accent}{ping}ms.")

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

    // PMS
    @Comment("Sent when you have no one to reply to")
    @Path("pms.reply.not_found")
    val PM_REPLY_NOT_FOUND = Property.create("{prefix_bad} You do not have any messages to reply to!")

    @Comment("When the person you are replying to has gone offline")
    @Path("pms.reply.offline")
    val PM_REPLY_OFFLINE = Property.create("{prefix_bad} This player is no longer online!")

    @Path("pms.format.received")
    val PRIVATE_MESSAGE_RECEIVED_FORMAT = Property.create(
        "<click:suggest_command:/msg %moducore_plain_displayname%>{color_gray}({color_neutral}%moducore_displayname% " +
                "{color_gray}-> {color_neutral}you{color_gray}) {color_white}{message}"
    )

    @Path("pms.format.sent")
    val PRIVATE_MESSAGE_SENT_FORMAT = Property.create(
        "<click:suggest_command:/msg %moducore_plain_displayname%>{color_gray}({color_neutral}you {color_gray}-> " +
                "{color_neutral}%moducore_displayname%{color_gray}) {color_white}{message}"
    )

    @Path("pms.format.socialspy")
    val SOCIAL_SPY_FORMAT = Property.create(
        "{color_red}[SPY] {color_gray}({color_neutral}{sender} {color_gray}-> {color_neutral}{target}{color_gray}) " +
                "{color_white}{message}"
    )

    @Path("pms.socialspy.enabled")
    val SOCIAL_SPY_ENALBED = Property.create("{prefix_good} Enabled Social Spy!")

    @Path("pms.socialspy.disabled")
    val SOCIAL_SPY_DISABLED = Property.create("{prefix_bad} Disabled Social Spy!")

    // SUDO
    @Path("sudo")
    val SUDO =
        Property.create("{prefix_good} Forcing {color_name}%moducore_displayname% {color_green}to run {color_accent}{command}.")

    // RELOAD
    @Path("reload.success")
    val RELOAD_SUCCESS = Property.create("{prefix_good} Successfully reloaded ModuCore! All changes have taken effect.")

    // MORE
    @Path("more.success")
    val MORE_SUCCESS = Property.create("{prefix_good} Successfully stacked this to {color_accent}x{amount}!")

    // NEAR
    @Path("near.success")
    val NEAR_SUCCESS =
        Property.create("{prefix_neutral} {color_name}%moducore_displayname% {color_neutral}is {color_accent}{distance} {color_neutral}blocks away.")

    @Path("near.failure")
    val NEAR_FAILURE =
        Property.create("{prefix_bad} No other players were found near you!")

    // TPS
    @Path("tps")
    val TPS = Property.create("{prefix_neutral} Server TPS: {tps}")

    // TOP
    @Path("top.success")
    val TOP = Property.create("{prefix_good} Successfully teleported you to the highest block!")

    // TIME
    @Path("time.success")
    val TIME_SUCCESS = Property.create("{prefix_good} Successfully set the time to {color_accent}{time}.")

    // WEATHER
    @Path("weather.success")
    val WEATHER_SUCCESS = Property.create("{prefix_good} Successfully set the weather to {color_accent}{weather}.")

    @Path("ptime.reset")
    val PTIME_RESET = Property.create("{prefix_good} Successfully reset your time to the server's time!")

    @Path("ptime.success")
    val PTIME_SUCCESS = Property.create("{prefix_good} Successfully set your time to {color_accent}{time}.")

    @Path("pweather.reset")
    val PWEATHER_RESET = Property.create("{prefix_good} Successfully reset your weather to the server's weather!")

    @Path("pweather.success")
    val PWEATHER_SUCCESS = Property.create("{prefix_good} Successfully set your weather to {color_accent}{weather}.")

    @Comment(
        "Discord Invite command message.",
        "NOTE: Many discord messages are in a separate lang file inside the discord folder."
    )
    @Path("discord.invite")
    val DISCORD_INVITE = Property.create("{prefix_neutral} Discord Invite Link: {color_accent}{link}")

    @Comment(
        "Discord Link Code Message.",
        "NOTE: Many discord messages are in a separate lang file inside the discord folder."
    )
    @Path("discord.link_code")
    val DISCORD_LINK_CODE = Property.create(
        "{prefix_neutral} Join the discord server at {color_accent}{link}{color_neutral}, then run " +
                "{color_accent}/link {code}{color_neutral}. {color_red}Do NOT share this code!"
    )
}