package net.dev.art.core.libs.server.nms

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.lang.reflect.Method

class ReflectionUtil {

    init {
        versionRaw = Bukkit.getServer()::class.java.getPackage().name.substring(23)
        versionMajor = Integer.valueOf(getVersionRawPart(0).substring(1))
        versionMinor = Integer.valueOf(getVersionRawPart(1))
        versionRelease = Integer.valueOf(getVersionRawPart(2).substring(1))
    }

    companion object {

        var versionRaw: String? = null
        var versionMajor = 0
        var versionMinor = 0
        var versionRelease = 0

        fun getNMSClass(nmsClassName: String): Class<*>? {
            return try {
                Class.forName("net.minecraft.server.$versionRaw.$nmsClassName")
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                null
            }
        }

        fun getOBCClass(obcClassName: String): Class<*>? {
            return try {
                Class.forName("org.bukkit.craftbukkit.$versionRaw.$obcClassName")
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                null
            }
        }

        fun getMethod(clazz: Class<*>, methodName: String?, vararg params: Class<*>?): Method? {
            return try {
                clazz.getMethod(methodName, *params)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun sendPacket(player: Player, packet: Any?) {
            try {
                val handle = player.javaClass.getMethod("getHandle", *arrayOfNulls<Class<*>?>(0)).invoke(
                    player,
                    *arrayOfNulls(0)
                )
                val playerConnection = handle.javaClass.getField("playerConnection")[handle]
                playerConnection.javaClass.getMethod("sendPacket", getNMSClass("Packet"))
                    .invoke(playerConnection, packet)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getVersionRawPart(index: Int): String {
            val versionRaw = versionRaw
            val parts = versionRaw!!.split("_".toRegex()).toTypedArray()
            return parts[index]
        }
    }

}