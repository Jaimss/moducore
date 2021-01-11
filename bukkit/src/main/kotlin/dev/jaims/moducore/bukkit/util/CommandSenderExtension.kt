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

package dev.jaims.moducore.bukkit.util

import dev.jaims.mcutils.bukkit.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Lang
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Send a message to a [CommandSender] telling them they have no permission.
 * @see [Lang.NO_PERMISSION]
 */
internal fun CommandSender.noPerms(node: String) {
    val fileManager = JavaPlugin.getPlugin(ModuCore::class.java).api.fileManager
    send(fileManager.getString(Lang.NO_PERMISSION, this as? Player).replace("{permission}", node))
}

/**
 * Send a usage to a player for a given command.
 * Header
 * [usage]
 * [description]
 */
internal fun CommandSender.usage(usage: String, description: String) {
    val fileManager = JavaPlugin.getPlugin(ModuCore::class.java).api.fileManager
    send(
        listOf(
            "&b&lModuCore &7- &cInvalid Usage",
            fileManager.getString(Lang.HELP_COMMAND_USAGE, this as? Player)
                .replace("{usage}", usage),
            fileManager.getString(Lang.HELP_COMMAND_DESCRIPTION, this as? Player)
                .replace("{description}", description)
        )
    )
}

/**
 * Send a message to a sender that the number was invalid.
 */
internal fun CommandSender.invalidNumber() {
    val fileManager = JavaPlugin.getPlugin(ModuCore::class.java).api.fileManager
    send(fileManager.getString(Lang.INVALID_NUMBER, this as? Player))
}

/**
 * The command is not a console command!
 */
internal fun CommandSender.noConsoleCommand() {
    val fileManager = JavaPlugin.getPlugin(ModuCore::class.java).api.fileManager
    send(fileManager.getString(Lang.NO_CONSOLE_COMMAND, this as? Player))
}

/**
 * Tell a [CommandSender] that their target player was not found online!
 */
internal fun CommandSender.playerNotFound(name: String) {
    val fileManager = JavaPlugin.getPlugin(ModuCore::class.java).api.fileManager
    send(fileManager.getString(Lang.TARGET_NOT_FOUND).replace("{target}", name))
}

