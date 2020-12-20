package dev.jaims.jcore.api.event

import org.bukkit.command.CommandSender
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * The [Event] called when JCore is reloaded.
 *
 * @param executor - who ran the command to reload the plugin
 */
class JCoreReloadEvent(executor: CommandSender) : Event() {

    override fun getHandlers(): HandlerList {
        return HandlerList()
    }

}