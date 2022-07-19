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

package dev.jaims.moducore.api.manager.player

import dev.jaims.moducore.api.manager.StorageManager
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import java.util.*

interface NameManager {

    /**
     * Get a players name from their [uuid] - Sample shows a join listener that uses the [getName]
     * method.
     *
     * @param uuid the UUID of the [Player] whose name you want to get.
     *
     * @return the name of the player. Will use the players nickname if it is available as well as color codes
     * if the player is online and has permission. If the player is offline, simply the raw name data will be returned
     */
    fun getName(uuid: UUID): Component

    /**
     * Set a players nickname.
     *
     * @param uuid the uuid of the player whose nickname we are changing
     * @param nickName the new nickame or null to remove it
     * @param storageManager the storage manager
     * @param silent if a target player exists, they will recieve a message if this is true
     */
    fun setNickName(
        uuid: UUID,
        nickName: String?,
        silent: Boolean,
        storageManager: StorageManager,
        executor: CommandSender? = null
    )
}