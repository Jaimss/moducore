package dev.jaims.moducore.api.event.util

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class ModuCoreEvent(async: Boolean = false) : Event(async) {

    companion object {
        @JvmStatic
        private val HANDLERS_LIST = HandlerList()

        /**
         * Get handlers
         */
        @JvmStatic
        fun getHandlerList() = HANDLERS_LIST
    }

    override fun getHandlers(): HandlerList = HANDLERS_LIST

}