package net.dev.art.core.libs.utils

import net.dev.art.core.libs.server.nms.ReflectionUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.function.Consumer

class ActionBar(private val text: String) {


    init {
        val constructor = ReflectionUtil.getNMSClass("PacketPlayOutChat")
            ?.getConstructor(ReflectionUtil.getNMSClass("IChatBaseComponent"), java.lang.Byte.TYPE)
        val icbc = ReflectionUtil.getNMSClass("IChatBaseComponent")!!.declaredClasses[0]
            .getMethod("a", String::class.java).invoke(null, "{\"text\":\"$text\"}")
        val packet: Any = constructor?.newInstance(icbc, 2.toByte())!!
        STATIC.packet = packet
    }

    companion object STATIC {
        var packet: Any? = null

        fun sendToPlayer(p: Player) {
            ReflectionUtil.sendPacket(p, packet)
        }

        fun sendToAll() = Bukkit.getOnlinePlayers().forEach { sendToPlayer(it) }

        fun sendToAllInAList(playersName: List<String>) {
            playersName.forEach(Consumer { playerName: String ->
                val player = Bukkit.getPlayer(playerName)
                if (player != null) {
                    ReflectionUtil.sendPacket(player, packet)
                }
            })
        }

        fun sendToAllInList(players: List<Player>) {
            val names = mutableListOf<String>()
            val list = players.forEach { names.add(it.name) }
            sendToAllInAList(names)
        }

        fun sendToAllWithPerm(permission: String) {
            Bukkit.getOnlinePlayers().stream().filter { Player: Player ->
                Player.hasPermission(
                    permission
                )
            }.forEach { Player: Player -> ReflectionUtil.sendPacket(Player, packet) }
        }

    }

}