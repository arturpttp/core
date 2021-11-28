package net.dev.art.core.libs.server.minigames

import org.bukkit.entity.Player

interface Minigame {

    val name: String
    val state: MinigameState
    val time: Long
    val timeToStart: Long
    val minPlayers: Int
    val maxPlayers: Int
    val players: MutableList<GamePlayer>
    val spectators: MutableList<GamePlayer>
    val rooms: MutableList<GameRoom>
    val cooldownTask: MinigameCooldown

    fun run()

    fun start()

    fun join(player: Player)
    fun join(player: Player, room: GameRoom)

    fun quit(player: GamePlayer)

}