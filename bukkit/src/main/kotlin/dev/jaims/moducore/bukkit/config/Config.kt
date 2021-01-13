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

object Config : SettingsHolder
{

    @Comment("What activates the 'chat ping'. Defaults to @PlayerName")
    @Path("chatping.activator")
    val CHATPING_ACTIVATOR = Property.create("@%moducore_displayname%")

    @Comment("What the format will be after the activation. Defaults to {color_name}@PlayerName")
    @Path("chatping.format")
    val CHATPING_FORMAT = Property.create("{color_name}@%moducore_displayname%")

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

    @Comment("The title of the /dispose command inventory")
    @Path("dispose.title")
    val DISPOSE_TITLE = Property.create("{color_green}Dispose Items")

    @Comment("The size of the /dispose command inventory (in rows) (must be 1-6)")
    @Path("dispose.size")
    val DISPOSE_SIZE = Property.create(4)
}