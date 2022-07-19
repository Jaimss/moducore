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

package dev.jaims.moducore.bukkit.func

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

/**
 * A listener extension that lets me easily call the onEvent method
 */
interface ListenerExt<T : Event> : Listener {
    fun onEvent(event: T)
}

/**
 * Wait for an event to occur, then unregister it on a timeout or when the event occurs once. Useful for listening to chat
 * messages for example.
 *
 * @param timeoutTicks the time to wait for in ticks
 * @param ignoreCancelled should it ignore cancelled
 * @param priority the priority of the listener
 * @param action the action to run when the even occurs
 * @param predicate the check that the event must meet
 * @param timeoutAction the action if you timout
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Event> JavaPlugin.waitForEvent(
    timeoutTicks: Long = -1,
    ignoreCancelled: Boolean = false,
    priority: EventPriority = EventPriority.NORMAL,
    crossinline predicate: (T) -> Boolean = { true },
    crossinline timeoutAction: () -> Unit = {},
    crossinline action: (T) -> Unit,
) {
    var task: BukkitTask? = null

    // a listener for the event
    val listener = object : ListenerExt<T> {
        override fun onEvent(event: T) {
            if (!predicate(event)) return
            action(event)
            task?.cancel()
            HandlerList.unregisterAll(this)
        }
    }
    // register the event itself
    server.pluginManager.registerEvent(
        T::class.java,
        listener,
        priority,
        { l, e -> (l as ListenerExt<T>).onEvent(e as T) },
        this@waitForEvent,
        ignoreCancelled
    )

    // if they want a timeout, start a timeout
    if (timeoutTicks > 0) {
        task = async(timeoutTicks * 20) {
            HandlerList.unregisterAll(listener)
            timeoutAction()
        }
    }
}

/**
 * Listen for an event as long as [this] plugin is Enabled. The [predicate] is optional. It can be nice for quick events,
 * but it also defaults to true to allow for customization.
 *
 * You can also set [ignoreCancelled] and [priority]
 * When this is detected, if the [predicate] is matched, the [action] is run
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Event> JavaPlugin.listenForEvent(
    ignoreCancelled: Boolean = false,
    priority: EventPriority = EventPriority.NORMAL,
    crossinline predicate: (T) -> Boolean = { true },
    crossinline action: (T) -> Unit
) {

    val listener = object : ListenerExt<T> {
        override fun onEvent(event: T) {
            if (!predicate(event)) return
            action(event)
        }
    }

    server.pluginManager.registerEvent(
        T::class.java,
        listener,
        priority,
        { l, e -> (l as ListenerExt<T>).onEvent(e as T) },
        this,
        ignoreCancelled
    )

}
