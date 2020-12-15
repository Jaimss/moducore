package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.command.HelpCommand
import dev.jaims.jcore.bukkit.command.allCommands
import dev.jaims.jcore.bukkit.command.fly.FlyCommand
import me.bristermitten.pdm.PluginDependencyManager

/**
 * Managers class to avoid clutter in the main class
 */
class Managers(private val plugin: JCore) {
    val playerManager: PlayerManager = PlayerManager(plugin)
}

/**
 * Method to register the events of the plugin
 */
internal fun registerEvents(plugin: JCore) {

}

/**
 * Method to register the commands.
 */
internal fun registerCommands(plugin: JCore) {
    allCommands.add(HelpCommand(plugin))
    allCommands.add(FlyCommand(plugin))

    allCommands.forEach {
        it.register(plugin)
    }


}

/**
 * Download dependencies using PDM to decrease Jar Size!
 */
internal fun pdmDependencySetup(plugin: JCore) {
    val pdm = PluginDependencyManager.of(plugin)
    pdm.loadAllDependencies().join()
}