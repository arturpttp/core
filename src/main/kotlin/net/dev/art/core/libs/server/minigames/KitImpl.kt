package net.dev.art.core.libs.server.minigames

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class KitImpl(
    override val name: String,
    override val description: Array<String>,
    override val icon: ItemStack,
    override val items: MutableList<ItemStack>,
    override val price: Double,
    override val cooldown: Long
) : Kit {

    override fun setup() {}

    override fun give(player: Player) {}

}