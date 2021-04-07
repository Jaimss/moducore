package dev.jaims.moducore.api.event.util

import org.bukkit.event.Cancellable

open class ModuCoreCancellableEvent(async: Boolean = true) : ModuCoreEvent(async), Cancellable {
    private var cancel = false

    override fun isCancelled(): Boolean = cancel

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}