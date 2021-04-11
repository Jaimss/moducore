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
import me.mattstudios.config.properties.ListProperty
import me.mattstudios.config.properties.Property
import me.mattstudios.config.properties.types.PropertyType

object Config : SettingsHolder {

    @Comment("What activates the 'chat ping'. Defaults to @PlayerName")
    @Path("chatping.activator")
    val CHATPING_ACTIVATOR = Property.create("@%moducore_displayname%")

    @Comment("What the format will be after the activation. Defaults to {color_name}@PlayerName")
    @Path("chatping.format")
    val CHATPING_FORMAT = Property.create("{color_name}@%moducore_displayname%")

    @Comment("The name of the default home. This is the name if no home name is provided to the command.")
    @Path("home.default_name")
    val HOME_DEFAULT_NAME = Property.create("default")

    @Comment("The time in seconds that a player has to type `undo`, cancelling the sethome they just did.")
    @Path("home.undo_timeout")
    val HOME_UNDO_TIMEOUT = Property.create<Long>(10)

    @Comment(
        "What lang file you want to use. Currently the default is set at en_US and that is what will be generated, no matter what you put here,",
        "however, if you would like the ability to switch between different lang files you can make some new ones here and choose which one to use",
        "If you would like to create a lang file, you can submit one to github. Please create an issue at https://github.com/Jaimss/moducore/issues ",
        "So we can discuss.",
        "NOTE: This will not be reset with a /moducorereload. You will need to restart the server for this to take effect."
    )
    @Path("lang_file")
    val LANG_FILE = Property.create("en-US")

    @Comment(
        "Should the target of commands be alerted about what happened.",
        "For example, if an admin heals a player, should they be notified that they were healed.",
        "If you want them notified, leave this as true. if you don't want them notified, set it to false.",
        "NOTE: You can append `-s` or `--silent` at any point to any command and the target player will not be notified.",
        "If you use `-s` or `--silent` on a command which has no target, it will be ignored."
    )
    @Path("alert_target")
    val ALERT_TARGET = Property.create(true)

    @Comment(
        "The abbreviated name of different time periods. Used in things like the uptime to shorten how long it is.",
        "You can also change the color of the number and the abbreviation provided below."
    )
    @Path("time.abbreviations")
    val TIME_SHORT_NAME = Property.create(
        String::class.java, mutableMapOf(
            "year" to "yr",
            "month" to "mo",
            "week" to "w",
            "day" to "d",
            "hour" to "h",
            "minute" to "m",
            "second" to "s",
        )
    )

    @Comment("This is the color of the number in a time string. For example 3m 22s, the 3 and 22 would be this color. Hex allowed.")
    @Path("time.number_color")
    val TIME_NUMBER_COLOR = Property.create("&f")

    @Comment("This is the color of the abbreviation in a time string. For example 3m 22s, the m and s would be this color. Hex allowed.")
    @Path("time.abbreviation_color")
    val TIME_ABBREV_COLOR = Property.create("&7")

    @Comment("The title of the /dispose command inventory")
    @Path("dispose.title")
    val DISPOSE_TITLE = Property.create("{color_green}Dispose Items")

    @Comment("The size of the /dispose command inventory (in rows) (must be 1-6)")
    @Path("dispose.size")
    val DISPOSE_SIZE = Property.create(4)

    @Comment("Singular form of hte currency of your economy.")
    @Path("economy.currency_singular")
    val CURRENCY_SINGULAR = Property.create("dollar")

    @Comment("The plural form of whatever currency your economy is.")
    @Path("economy.currency_plural")
    val CURRENCY_PLURAL = Property.create("dollars")

    @Comment("The symbol of your server's currency.")
    @Path("economy.currency_symbol")
    val CURRENCY_SYMBOL = Property.create("$")

    @Comment("Set the maximum distance from 0,0 in the X direction.")
    @Path("randomtp.max_x")
    val RTP_MAX_X = Property.create(1_000.0)

    @Comment("Set the maximum distance from 0,0 in the Z direction.")
    @Path("randomtp.max_z")
    val RTP_MAX_Z = Property.create(1_000.0)

    @Comment("Should players teleport to spawn automatically on join")
    @Path("spawn_on_join")
    val SPAWN_ON_JOIN = Property.create(false)

    @Comment("The cooldown in seconds for a player to warp.")
    @Path("cooldown.warp")
    val WARP_COOLDOWN = Property.create(2)

    @Comment("The cooldown in seconds for a player to go to spawn.")
    @Path("cooldown.spawn")
    val SPAWN_COOLDOWN = Property.create(2)

    @Comment("The cooldown in seconds for a player to teleport to their home.")
    @Path("cooldown.home")
    val HOME_COOLDOWN = Property.create(2)

    @Comment("the cooldown in seconds before a tpa goes through")
    @Path("cooldown.tpa")
    val TPA_COOLDOWN = Property.create(2)

    @Comment("The time in seconds before a teleport request expires.")
    @Path("cooldown.teleport_request_expire")
    val COOLDOWN_TELEPORT_REQUEST = Property.create(120)

    @Comment("The group the server is available to. moducore.lockdown.join.<group> to join a specific group. set to \"none\" to have no lockdown")
    @Path("lockdown.group")
    val LOCKDOWN_GROUP = Property.create("none")

    @Path("join_kits")
    @Comment("The list of kits a player gets when they join for the first time")
    val JOIN_KITS = ListProperty(PropertyType.STRING, mutableListOf())
}