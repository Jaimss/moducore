package dev.jaims.moducore.bukkit.command.time

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import me.mattstudios.config.properties.Property
import org.bukkit.command.CommandSender

class PTimeCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        TODO("Not yet implemented")
    }

    override val module: Property<Boolean>?
        get() = TODO("Not yet implemented")
    override val usage: String
        get() = TODO("Not yet implemented")
    override val description: String
        get() = TODO("Not yet implemented")
    override val commandName: String
        get() = TODO("Not yet implemented")
}