package dev.jaims.jcore.bukkit.event.listener

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.config.Lang
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val plugin: JCore) : Listener {

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        // change the logout message for the player
        quitMessage = Lang.QUIT_MESSAGE.get(player)
    }

}