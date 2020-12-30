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

package dev.jaims.jcore.bukkit.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Comment
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object SignCommands : SettingsHolder
{

    @Comment(
        "In the following section, you will be able to specify commands that should be run when signs are clicked.",
        "If the first line of the sign is [path], /command will be run. See the format below",
        "sign_commands:",
        "   path: \"command\"",
        "These are commands that will be run by the player, and will be permission checked appropriately for the command.",
    )
    @Path("player_commands")
    val PLAYER_COMMANDS = Property.create(
        String::class.java,
        mutableMapOf(
            "Dispose" to "dispose"
        )
    )

    @Comment(
        "These are commands which will be ran by the console. PlaceholderAPI placeholders work for getting the name of the player who clicked the sign.",
        "Players will get the effects of the sign as long as the have permission to run sign commands."
    )
    @Path("console_commands")
    val CONSOLE_COMMANDS = Property.create(
        String::class.java,
        mutableMapOf(
            "Heal" to "heal %jcore_displayname%"
        )
    )
}