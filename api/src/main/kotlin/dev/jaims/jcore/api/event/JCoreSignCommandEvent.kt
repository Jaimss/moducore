package dev.jaims.jcore.api.event

import org.bukkit.block.Sign
import org.bukkit.command.CommandSender
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerInteractEvent

/**
 * An event called when a player clicks a sign to run a command.
 *
 * @param sender - the player/consolesender that ran the command. you can use an instanceOf check to see whether console or a player ran the command
 * @param command - the command that was ran by the player, completely unmodified.
 * @param actualCommand - the command that was ran by the player, colorized and with placeholders replaced appropriately
 * @param signClicked - the sign the player clicked. can be used to get coordinates/location, etc etc
 * @param interactEvent - the rest of the event data if you want it for any reason
 */
class JCoreSignCommandEvent(
    sender: CommandSender,
    command: String,
    actualCommand: String,
    signClicked: Sign,
    interactEvent: PlayerInteractEvent
) : Event() {

    override fun getHandlers(): HandlerList {
        return HandlerList()
    }

}