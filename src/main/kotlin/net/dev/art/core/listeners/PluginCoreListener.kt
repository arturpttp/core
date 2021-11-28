package net.dev.art.core.listeners

import net.dev.art.core.libs.PluginCore
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent

class PluginCoreListener(private val plugin: PluginCore) : Listener {

    init {
        plugin.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun enable(event: PluginEnableEvent) {
        if (event.plugin is PluginCore) {
            (event.plugin as PluginCore).onPluginEnable(event.plugin)
        }
    }

    @EventHandler
    fun disable(event: PluginDisableEvent) {
        if (event.plugin is PluginCore)
            (event.plugin as PluginCore).onPluginEnable(event.plugin)
    }

}