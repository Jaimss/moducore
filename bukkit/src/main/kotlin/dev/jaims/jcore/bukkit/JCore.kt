package dev.jaims.jcore.bukkit

import dev.jaims.jcore.bukkit.manager.Managers
import dev.jaims.jcore.bukkit.manager.pdmDependencySetup
import dev.jaims.jcore.bukkit.manager.registerCommands
import dev.jaims.jcore.bukkit.manager.registerEvents
import dev.jaims.mcutils.bukkit.log
import org.bukkit.plugin.java.JavaPlugin

class JCore : JavaPlugin() {

    lateinit var managers: Managers

    // plugin startup logic
    override fun onEnable() {
        log("&aJCore is starting...")

        // register all managers/commands/events
        pdmDependencySetup(this)
        managers = Managers(this)
        registerCommands(this)
        registerEvents(this)

        log("&aJCore enabled!")
    }

    // plugin shutdown logic
    override fun onDisable() {
        log("&cJCore disabling...")

        log("&cJCore disabled.")
    }

}