package dev.jaims.jcore.api.event

import me.mattstudios.mfmsg.bukkit.BukkitComponent
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * The Chat Event that JCore will call if chat is enabled in the modules.
 *
 * @param player the player who sent the message
 * @param originalMessage the original message without markdown formatting
 * @param message the bukkit message with formatting
 * @param recipients the players who will recieve the message
 */
@Suppress("MemberVisibilityCanBePrivate")
class JCoreAsyncChatEvent(
    val player: Player,
    val originalMessage: String,
    var message: BukkitComponent,
    var recipients: Set<Player>
) : Event(true), Cancellable
{

    override fun getHandlers(): HandlerList
    {
        return HANDLERS_LIST
    }

    companion object
    {

        private val HANDLERS_LIST = HandlerList()
    }

    private var isCancelled = false

    override fun isCancelled(): Boolean
    {
        return isCancelled
    }

    override fun setCancelled(cancel: Boolean)
    {
        isCancelled = cancel
    }
}