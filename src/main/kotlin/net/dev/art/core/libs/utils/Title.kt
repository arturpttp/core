package net.dev.art.core.libs.utils

import net.dev.art.core.libs.interfaces.Placeholdable
import org.bukkit.entity.Player

class Title(
    var title: String = "Title",
    var subTitle: String = "",
    var fadeIn: Int = 20,
    var stay: Int = 20,
    var fadeOut: Int = 20
): Placeholdable {


    fun create(player: Player) {
        TODO("send title method")
//        player.sendTitle(getReplaced(title), getReplaced(subTitle), fadeIn, stay, fadeOut)
    }
}