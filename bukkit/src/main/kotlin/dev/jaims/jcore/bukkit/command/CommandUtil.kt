package dev.jaims.jcore.bukkit.command

import dev.jaims.jcore.bukkit.JCore
import org.bukkit.command.CommandExecutor

interface JCoreCommand : CommandExecutor {

    // a usage for the command
    val usage: String

    // a description of what the command does
    val description: String

    // the name of the command
    val commandName: String

    /**
     * A method to register a [JCoreCommand]
     */
    fun register(plugin: JCore) {
        plugin.getCommand(commandName)?.setExecutor(this)
    }

}

val allCommands: MutableList<JCoreCommand> = mutableListOf()