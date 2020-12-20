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

package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.api.manager.PlayerManager
import dev.jaims.jcore.bukkit.JCore
import dev.jaims.mcutils.common.InputType
import dev.jaims.mcutils.common.getInputType
import dev.jaims.mcutils.common.getName
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class PlayerManagerImpl(private val plugin: JCore) : PlayerManager {

    /**
     * return a [Player] with [name]
     */
    internal fun getTargetPlayer(name: String): Player? {
        if (name.getInputType() == InputType.NAME) {
            return Bukkit.getPlayer(name)
        }
        return Bukkit.getPlayer(UUID.fromString(name))
    }

    /**
     * Get a list of players online on the server. Matches their name against a specified [input].
     */
    internal fun getPlayerCompletions(input: String): List<String> {
        val completions = mutableListOf<String>()
        for (p in Bukkit.getOnlinePlayers()) {
            val name = getName(p.uniqueId)
            if (name.contains(input, ignoreCase = true)) completions.add(name)
        }
        return completions
    }

    /**
     * Method to get a players name.
     * For Now, its just the displayname, but I wanted to add this method so its already being used when I verbosify it
     * to potentially use a database or something for nicknames.
     */
    override fun getName(uuid: UUID): String {
        return plugin.server.getPlayer(uuid)?.displayName ?: uuid.getName() ?: "null"
    }


}