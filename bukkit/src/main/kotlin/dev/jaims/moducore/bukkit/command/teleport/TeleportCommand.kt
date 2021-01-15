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

class TeleportCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/teleport <target> [player]"
    override val description: String = "Teleport to another player. If you provide a second player, the first player will teleport to them."
    override val commandName: String = "teleport"

    private val playerManager = plugin.api.playerManager
    private val fileManager = plugin.api.fileManager

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        when (args.size)
        {
            1 ->
            {
                if (!Perm.TELEPORT.has(sender)) return
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                // target exsists and the sender is a player
                sender.teleport(target.location)
                if (!props.isSilent)
                {
                    target.send(fileManager.getString(Lang.TELEPORT_PLAYER_TO_YOU, sender))
                }
                sender.send(fileManager.getString(Lang.TELEPORT_SUCCESS, target))
            }
            2 ->
            {
                if (!Perm.TELEPORT_PLAYER_TO_PLAYER.has(sender)) return
                val player = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return
                }
                val target = playerManager.getTargetPlayer(args[1]) ?: run {
                    sender.playerNotFound(args[1])
                    return
                }
                player.teleport(target)

                if (!props.isSilent)
                {
                    player.send(fileManager.getString(Lang.TELEPORT_SUCCESS, target))
                    target.send(fileManager.getString(Lang.TELEPORT_PLAYER_TO_YOU, player))
                }
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
            2 -> matches.addAll(playerManager.getPlayerCompletions(args[1]))
        }

        return matches
    }


}