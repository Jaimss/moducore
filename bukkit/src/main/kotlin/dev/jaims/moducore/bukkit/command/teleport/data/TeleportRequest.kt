package dev.jaims.moducore.bukkit.command.teleport.data

import com.okkero.skedule.CoroutineTask
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class TeleportRequest(
    val sender: Player,
    val target: Player,
    val plugin: ModuCore,
    val sendTime: Date
) {
    companion object {
        val REQUESTS = mutableSetOf<TeleportRequest>()
    }

    // cancel this job when a request is denied or accepted
    val job: CoroutineTask = Bukkit.getScheduler().schedule(plugin, SynchronizationContext.ASYNC) {
        waitFor(this@TeleportRequest.plugin.api.fileManager.config[Config.COOLDOWN_TELEPORT_REQUEST] * 20L)
        // remove it if the job isn't yet cancelled
        REQUESTS.remove(this@TeleportRequest)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is TeleportRequest) return false
        if (sender == other.sender && target == other.target) return true
        return false
    }

    override fun hashCode(): Int {
        var result = sender.hashCode()
        result = 31 * result + target.hashCode()
        result = 31 * result + plugin.hashCode()
        result = 31 * result + sendTime.hashCode()
        return result
    }

}