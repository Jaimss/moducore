package dev.jaims.jcore.bukkit.command

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.mcutils.bukkit.register
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class HelpCommand(private val plugin: JCore) : CommandExecutor {

    init {
        plugin.register(this, "help")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        return true
    }

}