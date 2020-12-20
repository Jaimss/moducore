package dev.jaims.jcore.example.listener

import dev.jaims.jcore.example.ExamplePlugin
import dev.jaims.mcutils.bukkit.event.waitForEvent
import dev.jaims.mcutils.bukkit.send
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener(private val plugin: ExamplePlugin) : Listener {

    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        // example of sending a player "Hi, name" the first time they say "Hello" after they join.
        plugin.waitForEvent<AsyncPlayerChatEvent>(
            predicate = { it.player.uniqueId == player.uniqueId && it.message == "Hello" },
            action = { player.send("Hi, ${plugin.jCoreAPI.playerManager.getName(player.uniqueId)}") }
        )
    }

}