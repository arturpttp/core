package net.dev.art.core.libs.utils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

open class ItemBuilder : ItemStack {

    private var slot = -1

    constructor() {
        type = Material.AIR
        amount = 1
        durability = 0.toShort()
        if (!itemMeta.hasLore()) {
            val meta = itemMeta
            meta.lore = ArrayList()
            itemMeta = meta
        }
    }

    constructor(material: Material) {
        type = material
        amount = 1
        durability = 0.toShort()
        if (!itemMeta.hasLore()) {
            val meta = itemMeta
            meta.lore = ArrayList()
            itemMeta = meta
        }
    }

    constructor(item: ItemStack) : this(item.type) {
        amount = item.amount
        data = item.data
        if (item.hasItemMeta()) itemMeta = item.itemMeta
        durability = item.durability
    }

    fun slot(slot: Int): ItemBuilder {
        this.slot = slot
        return this
    }

    fun name(name: String): ItemBuilder {
        val meta = itemMeta
        meta.displayName = (name.replace("&", "§"))
        itemMeta = meta
        return this
    }

    fun lore(vararg lore: String): ItemBuilder {
        val meta = itemMeta
        meta.lore =
            Arrays.stream(lore).map { message: String? ->
                message!!
            }.collect(Collectors.toList())
        itemMeta = meta
        return this
    }

    fun lore(lore: List<String?>): ItemBuilder {
        val meta = itemMeta
        meta.lore =
            lore.stream().map { message: String? ->
                message!!
            }.collect(Collectors.toList())
        itemMeta = meta
        return this
    }

    fun head(player: String?): ItemBuilder {
        type = Material.SKULL_ITEM
        val meta = itemMeta as SkullMeta
        meta.owner = player
        itemMeta = meta
        return meta(3)
    }

    fun addLore(line: String): ItemBuilder {
        val meta = itemMeta
        var lore = meta.lore
        if (lore == null) {
            lore = ArrayList()
        }
        lore.add((line))
        meta.lore = lore
        itemMeta = meta
        return this
    }

    fun glow(): ItemBuilder {
        return this;
        TODO("Create glow enchantment")
    }

    fun unbreakable(unbreakable: Boolean): ItemBuilder {
        val meta = itemMeta
        meta.spigot().isUnbreakable = unbreakable
        itemMeta = meta
        return this
    }

    fun meta(meta: Int): ItemBuilder {
        durability = meta.toShort()
        return this
    }

    fun meta(meta: Short): ItemBuilder {
        durability = meta
        return this
    }

    fun amount(amount: Int): ItemBuilder {
        setAmount(amount)
        return this
    }

    fun enchant(enchantment: Enchantment?, level: Int): ItemBuilder {
        addUnsafeEnchantment(enchantment, level)
        return this
    }

}
