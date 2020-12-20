package dev.jaims.jcore.example

import dev.jaims.jcore.api.JCoreAPI
import dev.jaims.mcutils.bukkit.log
import org.bukkit.plugin.java.JavaPlugin

class ExamplePlugin : JavaPlugin() {

    lateinit var jCoreAPI: JCoreAPI

    // enable logic gets the instance of the api
    // you can now use plugin.jCoreAPI.whatever
    override fun onEnable() {
        log("Startup for an example plugin, showing how to use the JCore API.")

        jCoreAPI = JCoreAPI.instance
    }

}