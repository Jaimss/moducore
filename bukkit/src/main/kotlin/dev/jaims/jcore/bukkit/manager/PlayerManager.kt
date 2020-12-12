package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.mcutils.bukkit.send
import dev.jaims.mcutils.common.InputType
import dev.jaims.mcutils.common.getInputType
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/**
 * Send a message to a [CommandSender] telling them they have no permission.
 * @see [Lang.NO_PERMISSION]
 */
internal fun CommandSender.noPerms() {
    send(Lang.NO_PERMISSION.get())
}

/**
 * Send a usage to a player for a given command.
 * Header
 * [usage]
 * [description]
 */
internal fun CommandSender.usage(usage: String, description: String) {
    send(
        listOf(
            "   &b&lJCore &7- &cInvalid Usage",
            "${Lang.PREFIX_BAD.get()} $usage",
            "${Lang.PREFIX_NEUTRAL.get()} $description"
        )
    )
}

/**
 * The command is not a console command!
 */
internal fun CommandSender.noConsoleCommand() {
    send(Lang.NO_CONSOLE_COMMAND.get())
}

/**
 * Tell a [CommandSender] that their target player was not found online!
 */
internal fun CommandSender.playerNotFound(name: String?) {
    when (name) {
        null -> {
            send(Lang.TARGET_NOT_FOUND.get())
        }
        else -> {
            send(Lang.TARGET_NOT_FOUND_WITH_NAME.get(name))
        }
    }
}

class PlayerManager(private val plugin: JCore) {

    /**
     * return a [Player] with [name]
     */
    fun getTargetPlayer(name: String): Player? {
        if (name.getInputType() == InputType.NAME) {
            return Bukkit.getPlayer(name)
        }
        return Bukkit.getPlayer(UUID.fromString(name))
    }

}