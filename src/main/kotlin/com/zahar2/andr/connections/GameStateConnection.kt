package com.zahar2.andr.connections

import com.zahar2.andr.data.GameState
import com.zahar2.andr.data.toGameState
import io.ktor.server.websocket.*

class GameStateConnection: Connection<GameState>() {

    private var gameState = GameState.SPLASH_SCREEN

    override fun String.toType(): GameState = this.toGameState()

    override suspend fun onNewConnection(session: DefaultWebSocketServerSession) {
        session.send(gameState)
    }

    override suspend fun update(data: GameState) { }

    suspend fun change(gameState: GameState) {
        if (this.gameState == gameState) return
        this.gameState = gameState
        broadcast(gameState)
    }
}