package dev.jaims.moducore.bukkit.command.teleport

import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.Perm
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.playerNotFound
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeleportHereCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/tphere <target>"
    override val description: String = "Teleport a person to you."
    override val commandName: String = "teleporthere"

    private val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        when (args.size)
        {
            1 ->
            {
                if (!Perm.TELEPORT_HERE.has(sender)) return
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                target.teleport(sender)
                if (!props.isSilent)
                {
                    target.send(fileManager.getString(Lang.TELEPORT_HERE_SUCCESS_TARGET, sender))
                }
                sender.setDisplayName(fileManager.getString(Lang.TELEPORT_HERE_SUCCESS, target))
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>
    {
        val matches = mutableListOf<String>()

        when (args.size)
        {
            1 -> matches.addAll(playerManager.getPlayerCompletions(args[0]))
        }

        return matches
    }
}