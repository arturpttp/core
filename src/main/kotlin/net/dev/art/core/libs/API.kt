package net.dev.art.core.libs

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.lang.reflect.Field
import java.net.URL
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.JarFile

class API {

    companion object {
        const val SEPARATOR = ":"
        const val LOCATION_SERIALIZE_FORMAT = ("X" + SEPARATOR + "Y" + SEPARATOR + "Z"
                + SEPARATOR + "YAW" + SEPARATOR + "PITCH" + SEPARATOR + "WORLD")
        var colorCodes = arrayOf(
            "&4", "§4", "&c", "§c", "&6", "§6", "&e", "§e",
            "&2", "§2", "&a", "§a", "&3", "§3", "&b", "§b",
            "&3", "§3", "&1", "§1", "&9", "§9", "&5", "§5",
            "&f", "§f", "&7", "§7", "&8", "§8", "&0", "§0",
            "&r", "§r", "&k", "§k", "&n", "§n", "&o", "§o"
        )

        fun console(message: String?) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message))
        }

        fun getIndexByItem(item: Any, array: Array<Any>): Int {
            var x = -1
            for (i in array.indices) {
                if (array[i] === item) {
                    x = i
                    break
                }
            }
            return x
        }

        fun replaceColorsCodes(string: String): String {
            var string = string
            for (colorCode in colorCodes) {
                string = string.replace(colorCode, "")
            }
            return string
        }

        fun serializeLocation(location: Location): String {
            return LOCATION_SERIALIZE_FORMAT.toLowerCase()
                .replace("x", location.x.toString() + "")
                .replace("y", location.y.toString() + "")
                .replace("z", location.z.toString() + "")
                .replace("yaw", location.yaw.toString() + "")
                .replace("pitch", location.pitch.toString() + "")
                .replace("world", location.world.name + "")
        }

        fun deserializeLocation(location: String): Location {
            val args = location.split(SEPARATOR.toRegex()).toTypedArray()
            return Location(
                Bukkit.getWorld(args[5]),
                args[0].toDouble(),
                args[1].toDouble(),
                args[2].toDouble(),
                args[3].toFloat(),
                args[4].toFloat()
            )
        }

        fun getTypeByName(name: String): EntityType? {
            var name = name
            name = name.replace(" ", "_")
            for (value in EntityType.values()) if (value.toString().equals(name, ignoreCase = true)) return value
            return null
        }

        fun toNumber(s: String?): Number? {
            try {
                NumberFormat.getInstance().parse(s)
            } catch (e: NumberFormatException) {
                return -1
            } catch (e: ParseException) {
                return -1
            }
            return 0
        }

        fun replace(toReplace: String, replaces: Array<String?>): String? {
            var s = toReplace
            for (replacer in replaces) {
                s = s.replace(replacer!!, "")
            }
            return s
        }

        fun isColumn(index: Int, column: Int): Boolean {
            return getColumn(index) == column
        }

        fun chancePercent(chance: Int): Boolean {
            var p = Random().nextInt(1)
            p *= 100
            return p < chance
        }

        fun getColumn(index: Int): Int {
            return if (index < 9) {
                index + 1
            } else index % 9 + 1
        }

        fun isNumeric(strNum: String?): Boolean {
            return if (strNum == null || strNum.isEmpty() || strNum.equals("nan", ignoreCase = true)) {
                false
            } else try {
                strNum.toInt()
                true
            } catch (nfe: NumberFormatException) {
                false
            }
        }

        fun getSlot(row: Int, column: Int): Int {
            return 9 * (row - 1) + (column - 1)
        }

        fun getClassesForPackage(plugin: JavaPlugin, pkgname: String): ArrayList<Class<*>>? {
            val classes = ArrayList<Class<*>>()
            val src = plugin.javaClass.protectionDomain.codeSource
            if (src != null) {
                val resource = src.location
                resource.path
                processJarfile(resource, pkgname, classes)
            }
            val names = ArrayList<String>()
            val classi = ArrayList<Class<*>>()
            for (classy in classes) {
                names.add(classy.simpleName)
                classi.add(classy)
            }
            classes.clear()
            Collections.sort(names, java.lang.String.CASE_INSENSITIVE_ORDER)
            for (s in names) for (classy in classi) {
                if (classy.simpleName == s) {
                    classes.add(classy)
                    break
                }
            }
            return classes
        }

        private fun loadClass(className: String): Class<*> {
            return try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                throw RuntimeException("Unexpected ClassNotFoundException loading class '$className'")
            }
        }

        private fun processJarfile(resource: URL, packageName: String, classes: ArrayList<Class<*>>) {
            val relPath = packageName.replace('.', '/')
            val resPath = resource.path.replace("%20", " ")
            val jarPath = resPath.replaceFirst("[.]jar[!].*".toRegex(), ".jar").replaceFirst("file:".toRegex(), "")
            val jarFile: JarFile = try {
                JarFile(jarPath)
            } catch (e: IOException) {
                throw RuntimeException("Unexpected IOException reading JAR File '$jarPath'", e)
            }
            val entries = jarFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryName = entry.name
                var className: String? = null
                if (entryName.endsWith(".class") && entryName.startsWith(relPath)
                    && entryName.length > relPath.length + "/".length
                ) {
                    className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "")
                }
                if (className != null) {
                    classes.add(loadClass(className))
                }
            }
        }

        fun canCallPlayer(target: String): Boolean {
            return try {
                Bukkit.getPlayer(target)
                true
            } catch (e: NullPointerException) {
                false
            }
        }

        fun getPrivateField(fieldName: String, clazz: Class<*>, `object`: Any): Any? {
            val field: Field
            var o: Any? = null
            try {
                field = clazz.getDeclaredField(fieldName)
                field.isAccessible = true
                o = field[`object`]
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return o
        }

        fun getDate(milliseconds: Long): String {
            val c = Calendar.getInstance()
            c.timeInMillis = milliseconds
            val format = SimpleDateFormat("dd/MM/yyyy")
            return format.format(c.time)
        }

        fun getDateAndTime(time: Long): String? {
            return "§c" + getDate(time) + " §cás§f " + getHours(time)
        }

        fun getHours(milliseconds: Long): String? {
            val c = Calendar.getInstance()
            c.timeInMillis = milliseconds
            val format = SimpleDateFormat("HH:mm:ss")
            return format.format(c.time)

        }

    }
}