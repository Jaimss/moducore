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

object Modules : SettingsHolder {

    @Comment("Set to false to disable all of the /gm commands")
    @Path("command.gamemode")
    val COMMAND_GAMEMODE = Property.create(true)

    @Comment("Set to false to disable the /nick and /unnick commands")
    @Path("command.nickname")
    val COMMAND_NICKNAME = Property.create(true)

    @Comment("set to false to disable the /repair and /repairall command")
    @Path("command.repair")
    val COMMAND_REPAIR = Property.create(true)

    @Comment("Set to false to disable all speed commands, /flyspeed, /speed, /walkspeed")
    @Path("command.speed")
    val COMMAND_SPEED = Property.create(true)

    @Comment("Set this to false if you want to disable all commands relating to /teleport")
    @Path("command.teleport")
    val COMMAND_TELEPORT = Property.create(true)

    @Comment(
        "This is very similar to the other teleport commands, it has just been set separately since there are often whole",
        "plugins made for randomly teleporting, you may want to only disable this teleport command."
    )
    @Path("command.random_teleport")
    val COMMAND_RANDOM_TELEPORT = Property.create(true)

    @Comment("Set to false to disable the /clearinventory command")
    @Path("command.clear_inventory")
    val COMMAND_CLEARINVENTORY = Property.create(true)

    @Comment("Set to false to disable the /dispose command")
    @Path("command.dispose")
    val COMMAND_DISPOSE = Property.create(true)

    @Comment("Set to false to disable the /feed command")
    @Path("command.feed")
    val COMMAND_FEED = Property.create(true)

    @Comment("Set to false to disable the /fly command")
    @Path("command.fly")
    val COMMAND_FLY = Property.create(true)

    @Comment("Set to false to disable the /give command")
    @Path("command.give")
    val COMMAND_GIVE = Property.create(true)

    @Comment("Set to false to disable the /heal command")
    @Path("command.heal")
    val COMMAND_HEAL = Property.create(true)

    @Comment("Set to false to disable /ping")
    @Path("command.ping")
    val COMMAND_PING = Property.create(true)

    @Comment("Set to false to disable the /help command. Can be useful if you want a custom help menu or something.")
    @Path("command.help")
    val COMMAND_HELP = Property.create(true)

    @Comment("Set to false to disable /tps")
    @Path("command.tps")
    val COMMAND_TPS = Property.create(true)

    @Comment("set to false to disable /time")
    @Path("command.time")
    val COMMAND_TIME = Property.create(true)

    @Comment("Setting this to false will disable all warps on the server.")
    @Path("warps")
    val COMMAND_WARPS = Property.create(true)

    @Comment("set to false to disable /weather")
    @Path("command.weather")
    val COMMAND_WEATHER = Property.create(true)

    @Comment(
        "This goes closely with the commands. It will disable the /spawn and /setspawn command as well as teleporing players to spawn",
        "when they join the server."
    )
    @Path("spawn")
    val SPAWN = Property.create(true)


    @Comment(
        "Set to false if you would like your own plugin to handle the chat.",
        "If you want support for the features like markdown chat, I suggest you checkout TriumphChat",
        "A plugin by the author of the library I used for markdown chat."
    )
    @Path("chat")
    val CHAT = Property.create(true)

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

    @Comment(
        "Set this to false if you want to disable ModuCore reigstering the economy. You can use this to allow another plugin",
        "to handle the storage and backend of the economy."
    )
    @Path("economy")
    val ECONOMY = Property.create(true)

    @Comment("Set this to false to disable all the discord bot features.")
    @Path("discordbot")
    val DISCORD_BOT = Property.create(true)
}