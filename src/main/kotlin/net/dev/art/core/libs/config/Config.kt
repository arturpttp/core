package net.dev.art.core.libs.config

import org.bukkit.Location
import org.bukkit.inventory.ItemStack

interface Config : SpecificSimpleStorage<Any> {

    fun getItem(path: String): ItemStack
    fun setItem(path: String, item: ItemStack)

    fun getLocation(path: String): Location
    fun setLocation(path: String, location: Location)
}
