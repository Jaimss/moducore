package dev.jaims.moducore.bukkit.command.teleport.request

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.command.teleport.data.TeleportRequest
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.*
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class TeleportRequestCommand(override val plugin: ModuCore) : BaseCommand {
    /**
     * The method to execute a command.
     *
     * @param sender the sender of the command
     * @param args the list of arguments that were provided by the player
     * @param props the [CommandProperties]
     */
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }
        if (!Permissions.TELEPORT_REQUEST.has(sender)) return

        val targetName = args.getOrNull(0) ?: run {
            sender.usage(usage, description)
            return
        }

        val target = playerManager.getTargetPlayer(targetName) ?: run {
            sender.playerNotFound(targetName)
            return
        }

        val req = TeleportRequest(sender, target, plugin, Date())
        if (TeleportRequest.REQUESTS.contains(req)) {
            sender.send(Lang.TELEPORT_REQUEST_ALREADY_SENT, target)
            return
        }
        TeleportRequest.REQUESTS.add(req)

        target.send(Lang.TELEPORT_REQUEST_RECEIVED, sender)
        sender.send(Lang.TELEPORT_REQUEST_SENT, target)
    }

    override val module: Property<Boolean> = Modules.COMMAND_TELEPORT
    override val usage: String = "/tpa <player>"
    override val description: String = "Request to teleport to a player."
    override val commandName: String = "teleportrequest"
    override val aliases: List<String> = listOf("/tpa", "/tpr")
}