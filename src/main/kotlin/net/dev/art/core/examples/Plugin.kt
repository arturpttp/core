package net.dev.art.core.examples

import net.dev.art.core.libs.PluginCore
import net.dev.art.core.libs.color
import net.dev.art.core.libs.config.BukkitConfig
import net.dev.art.core.libs.config.JsonConfig
import net.dev.art.core.libs.console
import net.dev.art.core.libs.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/*Created in 06:34 at 25/11/2021 in Programming dreams By artur*/
class Plugin : PluginCore("Plugin's Name", Plugin::class.java) {

    val bukkitConfig = BukkitConfig("messages.yml", this)
    val jsonConfig = JsonConfig("messages.json", this)

    override fun onEnable() {
        bukkitConfig.setIfNotContains("Minigame.name", "Skywars")
        val minigameName: String = bukkitConfig.getOrDefault("Minigame.name", "Skywars") as String;
        bukkitConfig.save()
        console(// This method send a console message, it accepts arrays and multiple strings
            "Plugin initialized",
            "Version: 0.0.1",
            "Author: ArturDev"
        )
    }

    init {
        val head: ItemStack = ItemBuilder()
            .head("player name")
            .name("&aplayer name".color()) //for all strings you can call function String::color() that replaces & to §
            .lore(
                "&eThis player is &aOnline",
                "&eClick to send a message"
            )
        val item: ItemStack = ItemBuilder(Material.PAPER)
            .name("§aJournal")
            .glow()
            .unbreakable(true)
            .lore(
                "This belongs to an old adventurer that pass here"
            )
    }

}