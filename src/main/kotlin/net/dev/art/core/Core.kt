package net.dev.art.core

import net.dev.art.core.libs.PluginCore
import net.dev.art.core.libs.console
import net.dev.art.core.libs.systems.events.Events
import net.dev.art.core.listeners.PluginCoreListener

class Core : PluginCore(
    "Core",
    Core::class.java
) {

    companion object {
        lateinit var instance: Core
    }

    init {
        instance = this
    }

    override fun onEnable() {
        PluginCoreListener(this)
        registries.add(Events::class.java)
        console(
            " ",
            "§aInitializing Plugin §f$name",
            "§aVersion §f${description.version}",
            "§aAuthor §f${description.authors}",
            " "
        )
    }

}