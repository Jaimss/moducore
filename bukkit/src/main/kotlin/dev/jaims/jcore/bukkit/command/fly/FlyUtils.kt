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
        executor?.send(Lang.FLIGHT_ENABLED_TARGET.get(displayName))
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
        executor?.send(Lang.FLIGHT_DISABLED_TARGET.get(displayName))
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
