package com.zahar2.andr.connections

import com.zahar2.andr.data.UserScore
import com.zahar2.andr.data.toUserScore
import io.ktor.server.websocket.*
import java.util.*

class UserScoreConnection : Connection<UserScore>() {

    private val map = Collections.synchronizedMap<String, Float>(LinkedHashMap())

    override suspend fun onNewConnection(session: DefaultWebSocketServerSession) {
        map.forEach {
            session.send(UserScore(it.key, it.value))
        }
    }

    override fun String.toType(): UserScore = this.toUserScore()

    private suspend fun addUser(userScore: UserScore) {

        val name = userScore.name

        if (!map.containsKey(name)) {

            map[name] = 0f

            broadcast(UserScore(name, map[name]!!))
        }
    }

    override suspend fun update(data: UserScore) {

        if (data.points == null) {
            addUser(data)
        } else {
            if (map.containsKey(data.name)) {
                map[data.name] = data.points
                broadcast(data)
            }
        }
    }
}