package net.dev.art.core.listeners

import net.dev.art.core.libs.systems.events.Events
import org.bukkit.event.EventHandler
import org.bukkit.event.server.RemoteServerCommandEvent

class TestEvents: Events() {

    @EventHandler
    fun command(event: RemoteServerCommandEvent) {
        if (!event.command.equals("test"))
            return
        event.isCancelled = true
        event.sender.sendMessage("Testado!!!")
    }

}