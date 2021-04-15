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

package dev.jaims.moducore.api.data

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class Kit(
    val name: String,
    var cooldown: Int,
    var items: List<ItemStack>,
    var consoleCommands: List<String>,
    var playerCommands: List<String>,
    val kitInfo: KitInfo = KitInfo(name, "Your Custom Kit Description! Edit in kits.yml", "DIRT", false)
) {
    fun give(player: Player): Kit {
        // give items
        val extras = player.inventory.addItem(*items.toTypedArray())
        extras.forEach { (_, item) ->
            player.world.dropItem(player.location, item)
        }
        // run player commands
        playerCommands.forEach { command ->
            val success = player.performCommand(command.replace("{player}", player.name))
            if (!success) {
                Bukkit.getServer().logger.warning("Player Command \"$command\" was not executed successfully for kit $name and player ${player.name}")
            }
        }
        // run console commands
        consoleCommands.forEach { command ->
            val success = Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, command.replace("{player}", player.name))
            if (!success) {
                Bukkit.getServer().logger.warning("Player Command \"$command\" was not executed successfully for kit $name and player ${player.name}")
            }
        }
        return this
    }
}

data class KitInfo(
    val displayName: String,
    val description: String,
    val displayItem: String,
    val glow: Boolean
)
