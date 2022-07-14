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

package dev.jaims.moducore.bukkit.func

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.message.legacyColorize
import me.mattstudios.config.properties.Property
import me.mattstudios.msg.adventure.AdventureMessage
import me.mattstudios.msg.base.MessageOptions
import me.mattstudios.msg.base.internal.Format
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Send a message to a [CommandSender] telling them they have no permission.
 * @see [Lang.NO_PERMISSION]
 */
internal fun CommandSender.noPerms(node: String) =
    send(Lang.NO_PERMISSION, this as? Player) { it.replace("{permission}", node) }

/**
 * Send a usage to a player for a given command.
 * Header
 * [usage]
 * [description]
 */
internal fun CommandSender.usage(usage: String, description: String, header: Boolean = true) {
    val plugin = JavaPlugin.getPlugin(ModuCore::class.java)
    val fileManager = JavaPlugin.getPlugin(ModuCore::class.java).api.bukkitFileManager
    val message = if (header) listOf(
        fileManager.lang[Lang.INVALID_USAGE_HEADER],
        fileManager.lang[Lang.HELP_COMMAND_USAGE].replace("{usage}", usage)
            .replace("{description}", description).langParsed
    ) else listOf(
        fileManager.lang[Lang.HELP_COMMAND_USAGE].replace("{usage}", usage)
            .replace("{description}", description).langParsed
    )
    message.forEach { m ->
        val audience = if (this is Player) plugin.audience.player(this) else plugin.audience.sender(this)
        audience.sendMessage(adventureMessage.parse(m))
    }
}

/**
 * Send a message to a sender that the number was invalid.
 */
internal fun CommandSender.invalidNumber() = send(Lang.INVALID_NUMBER, this as? Player)

/**
 * The command is not a console command!
 */
internal fun CommandSender.noConsoleCommand() = send(Lang.NO_CONSOLE_COMMAND, this as? Player)

/**
 * Tell a [CommandSender] that their target player was not found online!
 */
internal fun CommandSender.playerNotFound(name: String) = send(Lang.TARGET_NOT_FOUND) { it.replace("{target}", name) }

val adventureMessage = AdventureMessage.create(MessageOptions.builder(Format.ALL).build())

/**
 * Send a message to a command sender. If it's a player, markdown will work!
 */
fun CommandSender.send(
    messageProperty: Property<String>,
    player: Player? = null,
    transform: (String) -> String = { it }
) {
    val plugin = JavaPlugin.getPlugin(ModuCore::class.java)
    val lang = plugin.api.bukkitFileManager.lang
    var message = lang[messageProperty].langParsed
    message = transform(message)

    val audience = if (this is Player) plugin.audience.player(this)
    else plugin.audience.sender(this)

    audience.sendMessage(adventureMessage.parse(message.legacyColorize(player)))
}