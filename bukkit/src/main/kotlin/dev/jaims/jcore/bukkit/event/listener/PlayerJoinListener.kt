package dev.jaims.jcore.bukkit.event.listener

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.config.Lang
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent

class PlayerJoinListener(private val plugin: JCore) : Listener {

    @EventHandler
    // called before PlayerJoinEvent
    fun PlayerLoginEvent.onLogin() {
    }

    @EventHandler
    // called after the PlayerLoginEvent
    fun PlayerJoinEvent.onJoin() {
        joinMessage = Lang.JOIN_MESSAGE.get(player)
    }

}