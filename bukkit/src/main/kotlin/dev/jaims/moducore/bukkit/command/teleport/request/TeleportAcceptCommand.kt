package dev.jaims.moducore.bukkit.command.teleport.request

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.command.teleport.data.TeleportRequest
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.send
import io.papermc.lib.PaperLib
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportAcceptCommand(override val plugin: ModuCore) : BaseCommand {

    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        if (!Permissions.TELEPORT_ACCEPT.has(sender)) return

        // get the request
        val request = TeleportRequest.REQUESTS.firstOrNull { it.target.uniqueId == sender.uniqueId } ?: run {
            sender.send(Lang.NO_PENDING_REQUESTS, sender)
            return
        }
        // tp the player
        PaperLib.teleportAsync(request.sender, request.target.location)
        // cancel the job
        request.job.cancel()
        // remove the request
        TeleportRequest.REQUESTS.remove(request)

        request.sender.send(Lang.YOUR_REQUEST_ACCEPTED, request.target)
        request.target.send(Lang.REQUEST_ACCEPTED, request.sender)
    }

    override val module: Property<Boolean> = Modules.COMMAND_TELEPORT
    override val usage: String = "/tpaccept"
    override val description: String = "Accept a teleport request."
    override val commandName: String = "teleportaccept"
    override val aliases: List<String> = listOf("tpaccept")
}