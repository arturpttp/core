package net.dev.art.core.libs.server.minigames

interface GameRoom {
    val state: MinigameState
    val time: Long
    val timeToStart: Long
    val minPlayers: Int
    val maxPlayers: Int
    val players: MutableList<GamePlayer>
    val spectators: MutableList<GamePlayer>
}