package com.zahar2.andr.plugins

import com.zahar2.andr.connections.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun <D> Routing.simpleWebSocket(path: String, connection: Connection<D>) {
    webSocket(path) {
        println("$path: Adding user!")
        connection.addConnection(this)
        try {
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                connection.update(receivedText)
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            println("$path: Removing!")
            connection.removingConnection(this)
        }
    }
}

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        val userScoreConnection = UserScoreConnection()
        val gameStateConnection = GameStateConnection()
        val questionConnection = QuestionConnection()
        val videoConnection = VideoConnection()
        val managerConnection = ManagerConnection(gameStateConnection, questionConnection, videoConnection)

        videoConnection.onVideoEnd = {
            managerConnection.next()
        }

        simpleWebSocket("/user", userScoreConnection)
        simpleWebSocket("/state", gameStateConnection)
        simpleWebSocket("/question", questionConnection)
        simpleWebSocket("/video", videoConnection)
        simpleWebSocket("/manager", managerConnection)
    }
}
