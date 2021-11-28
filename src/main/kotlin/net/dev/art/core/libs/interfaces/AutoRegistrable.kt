package net.dev.art.core.libs.interfaces

import net.dev.art.core.libs.PluginCore

interface AutoRegistrable {

    val loadMessage: String
    val type: Class<*>
    val notMatches: MutableList<Class<*>>

    fun register(plugin: PluginCore)

}