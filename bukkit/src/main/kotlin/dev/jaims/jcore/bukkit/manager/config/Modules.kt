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

object Modules : SettingsHolder {

    @Comment("Below are a list of modules for each command. If one is set to false, the command will not be registered.")
    @Path("command.gamemode")
    val COMMAND_GAMEMODE = Property.create(true)

    @Path("command.repair")
    val COMMAND_REPAIR = Property.create(true)

    @Path("command.clear_inventory")
    val COMMAND_CLEARINVENTORY = Property.create(true)

    @Path("command.dispose")
    val COMMAND_DISPOSE = Property.create(true)

    @Path("command.feed")
    val COMMAND_FEED = Property.create(true)

    @Path("command.fly")
    val COMMAND_FLY = Property.create(true)

    @Path("command.give")
    val COMMAND_GIVE = Property.create(true)

    @Path("command.heal")
    val COMMAND_HEAL = Property.create(true)

    @Path("command.help")
    val COMMAND_HELP = Property.create(true)

    @Comment(
        "Below is different chat functions. If set to false, they will not be active.",
        "Chat format is whether or not markdown will be used and if the format from the lang file will be used."
    )
    @Path("chat.format")
    val CHAT_FORMAT = Property.create(true)

    @Comment("The chat ping is whether players names will change color and they will be pinged when the activator is sent in chat.")
    @Path("chat.ping")
    val CHAT_PING = Property.create(true)

    @Comment("Set to false if you want another plugin to handle the login message. You can customize the format in the lang file.")
    @Path("join.message")
    val JOIN_MESSAGE = Property.create(true)

    @Comment("Set to false if you want another plugin to handle the quit message. You can customize the format in the lang file.")
    @Path("quit.message")
    val QUIT_MESSAGE = Property.create(true)

    @Comment("Should the `placeholders.yml` file be created and be used to create custom placeholders.")
    @Path("placeholders")
    val PLACEHOLDERS = Property.create(true)

    @Comment("Should sign commands from the `sign_commands.yml` file work.")
    @Path("sign_commands")
    val SIGN_COMMANDS = Property.create(true)
}