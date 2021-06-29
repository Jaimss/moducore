/*
 * This file is a part of ModuCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
    val sendTime: Date,
    val bypassCooldown: Boolean
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