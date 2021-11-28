package net.dev.art.core.libs.server.minigames

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

abstract class MinigameCooldown(val minigame: Minigame, val plugin: Plugin) : BukkitRunnable() {

    fun startTimer() {
        val seconds = 1
        val time: Long = (20 * seconds).toLong()
        runTaskTimerAsynchronously(plugin, time, 20)
    }

}