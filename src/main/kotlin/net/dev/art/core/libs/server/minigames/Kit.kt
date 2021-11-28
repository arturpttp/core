package net.dev.art.core.libs.server.minigames

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface Kit {

    val name: String
    val description: Array<String>
    val icon: ItemStack
    val items: MutableList<ItemStack>
    val price: Double
    val cooldown: Long

    fun setup()

    fun give(player: Player)

}