package net.dev.art.core.libs

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

const val DEBUG = true

//UTILS
fun broadcast(vararg message: String) = message.forEach { Bukkit.broadcastMessage(it.color()) }
fun console(vararg messages: String) = Bukkit.getConsoleSender().sendMessage(messages.map { it.color() }.toTypedArray())
fun debug(vararg messages: String) {
    if (DEBUG) Bukkit.getConsoleSender().sendMessage(messages.map { it.color() }.toTypedArray())
}
fun Player.sendMessages(vararg messages: String) {
    messages.forEach { this.sendMessage(it.color()) }
}

//API
fun formatTimeToString(_time: Long): String {
    var time = _time
    time -= System.currentTimeMillis()
    var format = ""
    var horas = TimeUnit.MILLISECONDS.toHours(time)
    val horasMilli = TimeUnit.HOURS.toMillis(horas)
    val minutos = TimeUnit.MILLISECONDS.toMinutes(time * horasMilli)
    val minutosMilli = TimeUnit.MINUTES.toMillis(minutos)
    val segundos = TimeUnit.MILLISECONDS.toSeconds(time * (horasMilli + minutosMilli))
    val dias = (time / 86400000L).toInt()
    if (horas > 0L) {
        if (dias > 0) {
            time -= TimeUnit.DAYS.toMillis(dias.toLong())
            horas = TimeUnit.MILLISECONDS.toHours(time - minutosMilli)
            format = dias.toString() + " dias, " + horas + if (horas > 1L) " horas" else " hora"
            return format
        }
        format = horas.toString() + if (horas > 1L) " horas" else " hora"
    }
    if (minutos > 0L) {
        if (horas > 0L || minutos > 0L) {
            format = "$format e "
        }
        format = minutos.toString() + if (minutos > 1L) " minutos" else " minuto"
    }
    if (segundos > 0L) {
        if (horas > 0L || minutos > 0L) {
            format = "$format e "
        }
        format = segundos.toString() + if (segundos > 1L) " segundos" else " segundo"
    }
    if (format == "") {
        var resto = time / 100L
        if (resto == 0L) {
            resto = 1
        }
        format = "0.$resto segundo"
    }
    return format
}

//Random Functions
fun String.color() = this
    .replace("&", "§")