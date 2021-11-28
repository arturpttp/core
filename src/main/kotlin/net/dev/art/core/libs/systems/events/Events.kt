package net.dev.art.core.libs.systems.events

import net.dev.art.core.libs.PluginCore
import net.dev.art.core.libs.color
import net.dev.art.core.libs.console
import net.dev.art.core.libs.interfaces.AutoRegistrable
import org.bukkit.event.Listener

open class Events : AutoRegistrable, Listener {
    override val loadMessage: String = ""
    override val type: Class<*> = Events::class.java
    override val notMatches: MutableList<Class<*>> = mutableListOf(
        Events::class.java,
        Listener::class.java
    )

    override fun register(plugin: PluginCore) {
        plugin.pluginManager.registerEvents(this, plugin)
        val split = javaClass.name.split(".")
        console("§aEventAPI §f-> &eLoading event §f${split[split.lastIndex]}...".color())
    }
}