package net.dev.art.core.libs.config

import net.dev.art.core.libs.debug
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.io.File

class BukkitConfig(
    val name: String,
    val plugin: Plugin
) : Config {
    //unused
    private val configPrefix = "§aConfigs §7-> "

    private var nameWithExtension: String
    var file: File
    var config: YamlConfiguration

    init {
        name.let {
            if (it.contains(".")) nameWithExtension = it
            else nameWithExtension = "$name.yml"
        }

        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
            debug("$configPrefix§eCreating §7${plugin.name}'s data folder")
        }
        file = File(plugin.dataFolder, nameWithExtension)

        if (file.exists()) {
            debug("$configPrefix§eLoading file §7$nameWithExtension")
        } else {
            file.createNewFile()
            debug("$configPrefix§eCreating file §7$nameWithExtension")
        }

        config = YamlConfiguration.loadConfiguration(file)

        debug("$configPrefix§eLoading cached data for file §7$nameWithExtension")
        save()
    }

    override val map: MutableMap<String, Any?> = mutableMapOf()

    fun save() {
        config.save(file)
        debug("$configPrefix§eSaving file §7$nameWithExtension")
    }

    fun saveDefault() {
        if (!file.exists()) {
            plugin.saveResource(nameWithExtension, false)
        }
    }

    override fun set(key: String, value: Any?) {
        config.set(key, value)
    }

    override fun get(key: String): Any? {
        return config.get(key)
    }

    override fun getOrDefault(key: String, default: Any?, autoSet: Boolean): Any? = if (!contains(key)) {
        if (autoSet) {
            set(key, default)
            get(key)
        }
        default
    } else get(key)

    override fun setIfNotContains(key: String, value: Any?) {
        if (!contains(key))
            set(key, value)
    }

    override fun getItem(path: String): ItemStack {
        TODO("Not yet implemented")
    }

    override fun setItem(path: String, item: ItemStack) {
        TODO("Not yet implemented")
    }

    override fun getLocation(path: String): Location {
        TODO("Not yet implemented")
    }

    override fun setLocation(path: String, location: Location) {
        TODO("Not yet implemented")
    }

    override fun contains(key: String): Boolean = config.contains(key)

    fun getString(key: String): String {
        return get(key).toString()
    }
    fun getInt(key: String): Int {
        return config.getInt(key)
    }
    fun getDouble(key: String): Double {
        return config.getDouble(key)
    }
    fun getFloat(key: String): Float {
        return getDouble(key).toFloat()
    }
    fun getLong(key: String): Long {
        return getDouble(key).toLong()
    }
    fun getBoolean(key: String): Boolean {
        return config.getBoolean(key)
    }

    fun getStringList(key: String): List<String>? {
        return config.getStringList(key)
    }
    fun getList(key: String): List<*>? {
        return config.getList(key)
    }

    fun getMap(key: String): Map<*, *>? {
        return null
    }

}