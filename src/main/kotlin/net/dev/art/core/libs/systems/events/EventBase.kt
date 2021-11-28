package net.dev.art.core.libs.systems.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class EventBase : Event() {

    companion object {

        private val handlerList: HandlerList = HandlerList()

        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }

    override fun getHandlers(): HandlerList? {
        return handlerList
    }

}