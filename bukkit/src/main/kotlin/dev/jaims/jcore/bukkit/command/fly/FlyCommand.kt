package dev.jaims.jcore.bukkit.command.fly

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.manager.noConsoleCommand
import dev.jaims.jcore.bukkit.manager.playerNotFound
import dev.jaims.jcore.bukkit.manager.usage
import dev.jaims.mcutils.bukkit.register
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand(private val plugin: JCore) : CommandExecutor {

    init {
        plugin.register(this, "fly")
    }

    private val usage = "/fly [target]"
    private val description = "Enable fly for yourself or another player."

    private val playerManager = plugin.managers.playerManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // invalid args length
        if (args.size > 1) {
            sender.usage(usage, description)
            return false
        }

        when (args.size) {
            // for a single player
            0 -> {
                if (!Perm.FLY.has(sender)) return false
                // only fly for Players
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return false
                }
                sender.toggleFlight()
            }
            // for a target player
            1 -> {
                if (!Perm.FLY_TARGET.has(sender)) return false
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return false
                }
                target.toggleFlight(sender)
            }
        }

        return true
    }

}