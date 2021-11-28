package net.dev.art.core.libs.server.minigames

import org.bukkit.entity.Player

class GamePlayer(
    val player: Player,
    val game: Minigame,
    val room: GameRoom
) {

    val name: String = player.name

}