package dev.jaims.jcore.bukkit.command

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.mcutils.bukkit.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpCommand(private val plugin: JCore) : JCoreCommand {
    override val usage: String = "/help [command]"
    override val description: String = "Show help menus for all commands or a specific one."
    override val commandName: String = "help"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        allCommands.forEach {
            sender.send(
                listOf(
                    it.usage,
                    "${Lang.PREFIX_NEUTRAL.get()}${it.description}"
                )
            )
        }

        return true
    }

}