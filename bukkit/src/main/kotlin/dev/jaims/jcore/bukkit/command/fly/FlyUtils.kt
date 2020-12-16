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

package dev.jaims.jcore.bukkit.command.fly

import dev.jaims.jcore.bukkit.event.event.JCoreFlightToggledEvent
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.mcutils.bukkit.send
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Enable flight for a [this] and optionally [sendMessage] to the player letting them know they
 * now have flight enabled.
 * If [executor] is null, the player changed their own flight. If [executor] is not null, someone else changed
 * their flight.
 *
 * @return True if they are now flying, false if they were already flying.
 */
internal fun Player.enableFlight(executor: CommandSender? = null, sendMessage: Boolean = true) {
    // set them to flying
    isFlying = true
    if (sendMessage) {
        send(Lang.FLIGHT_ENABLED.get())
        executor?.send(Lang.FLIGHT_ENABLED_TARGET.get().replace("{target}", displayName))
    }
    // call the fly event
    Bukkit.getPluginManager().callEvent(JCoreFlightToggledEvent(this, executor, true))
}

/**
 * Disable flight for a [this] and optionally [sendMessage] to the player letting them know they are no longer
 * flying.
 * If [executor] is null, the player changed their own flight. If [executor] is not null, someone else changed
 * their flight.
 *
 * @return True if they are no longer flying, false if they were already flying.
 */
internal fun Player.disableFlight(executor: CommandSender? = null, sendMessage: Boolean = true) {
    isFlying = false
    if (sendMessage) {
        send(Lang.FLIGHT_DISABLED.get())
        executor?.send(Lang.FLIGHT_DISABLED_TARGET.get().replace("{target}", displayName))
    }
    // call the fly event
    Bukkit.getPluginManager().callEvent(JCoreFlightToggledEvent(this, executor, false))
}

/**
 * Toggle flight using [disableFlight] and [enableFlight]
 */
internal fun Player.toggleFlight(executor: CommandSender? = null, sendMessage: Boolean = true) {
    if (isFlying) disableFlight(executor, sendMessage)
    else enableFlight(executor, sendMessage)
}
