package net.dev.art.core.libs

import net.dev.art.core.libs.interfaces.AutoRegistrable
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

open class PluginCore(
    name: String,
    override val providingClass: Class<out PluginCoreBase>
) : JavaPlugin(), PluginCoreBase {

    companion object {
        val registries: MutableList<Class<out AutoRegistrable>> = mutableListOf()
    }

    override val pluginName: String = name
    val pluginManager: PluginManager = Bukkit.getPluginManager()

    override fun getJavaPlugin(): JavaPlugin {
        return getProvidingPlugin(this::class.java)
    }

    override fun register() {
        val pkg = (providingClass.getPackage().toString().replace("package ", "") + ",").split(",")[0]

        val classesList = API.getClassesForPackage(getJavaPlugin(), pkg)!!

        debug(
            "§b$name §8» §eLoading §7${classesList.size}§e class(es)",
            "§b$name §8» §eRegistering §7${registries.size}§e type(s) of class(es)"
        )

        val toRegistry: MutableList<Class<out AutoRegistrable>> = mutableListOf()

        (classesList
            .filter {
                AutoRegistrable::class.java.isAssignableFrom(it) && it != AutoRegistrable::class.java && !it.isInterface
            } as ArrayList<Class<out AutoRegistrable>>)
            .forEach { cls ->
                val newInstance = cls.newInstance()
                newInstance.register(this)
                console(newInstance.loadMessage.color())
            }
    }


    override fun onPluginEnable(plugin: Plugin) {
        if (plugin is PluginCore) {
            plugin.register()
        }
    }

    override fun onPluginDisable(plugin: Plugin) {
        if (plugin is PluginCore) {
            TODO("Nothing, maybe, we can send a message")
        }
    }

}