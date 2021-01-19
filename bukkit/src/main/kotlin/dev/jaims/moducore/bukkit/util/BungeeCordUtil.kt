package dev.jaims.moducore.bukkit.util

import org.bukkit.Bukkit

/**
 * Util to check if we are running bungeecord.
 */
fun isBungeeEnabled(): Boolean {
    val config = Bukkit.getServer().spigot().spigotConfig
    return config.getBoolean("settings.bungeecord")
}