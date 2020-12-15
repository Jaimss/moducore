package dev.jaims.jcore.bukkit.manager.config

import dev.jaims.jcore.bukkit.JCore
import org.bukkit.plugin.java.JavaPlugin

enum class Config(override val path: String, override val default: Any): ConfigFileEnum {

    HOLDER("", "a");

    /**
     * Get the result as a string
     */
    fun getString(): String {
        return JavaPlugin.getPlugin(JCore::class.java).config.getString(path) ?: default.toString()
    }

    /**
     * Get any result
     */
    fun get(): Any {
        return JavaPlugin.getPlugin(JCore::class.java).config.get(path) ?: default
    }

}