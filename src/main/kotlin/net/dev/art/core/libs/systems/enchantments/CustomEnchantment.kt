package net.dev.art.core.libs.systems.enchantments

import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

/*Created in 05:27 at 13/11/2021 in Programming dreams By artur*/
abstract class CustomEnchantment(name: String, id: Int) : Enchantment(id) {

    override fun getName(): String = name

    abstract fun register(plugin: JavaPlugin)

}