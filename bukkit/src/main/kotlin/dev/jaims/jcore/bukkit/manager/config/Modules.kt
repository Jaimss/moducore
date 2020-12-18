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

import dev.jaims.jcore.bukkit.JCore
import org.bukkit.plugin.java.JavaPlugin

enum class Modules(override val path: String, override val default: Any) : ConfigFileEnum {

    // Modularized commands
    COMMAND_GAMEMODE("command.gamemode", true),
    COMMAND_CLEARINVENTORY("command.clear_inventory", true),
    COMMAND_FEED("command.feed", true),
    COMMAND_FLY("command.fly", true),
    COMMAND_GIVE("command.give", true),
    COMMAND_HEAL("command.heal", true),
    COMMAND_HELP("command.help", true),

    // chat modules
    CHAT_FORMAT("chat.format", true),
    CHAT_PING("chat.ping", true),

    // join modules
    JOIN_MESSAGE("join.message", true),

    // leave modules
    LEAVE_MESSAGE("leave.message", true);

    /**
     * Get a boolean value.
     */
    fun getBool(): Boolean {
        return JavaPlugin.getPlugin(JCore::class.java).managers.fileManager.modules.getBoolean(path)
    }
}