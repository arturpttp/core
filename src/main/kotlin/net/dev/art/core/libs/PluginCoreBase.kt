package net.dev.art.core.libs

import net.dev.art.core.libs.interfaces.AutoRegistrable
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

interface PluginCoreBase {

    val pluginName: String
    val providingClass: Class<out PluginCoreBase>

    fun register()

    fun getJavaPlugin(): JavaPlugin

    fun onPluginEnable(plugin: Plugin)
    fun onPluginDisable(plugin: Plugin)

}