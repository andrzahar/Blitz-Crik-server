package com.zahar2.andr.connections

import com.zahar2.andr.data.Game
import com.zahar2.andr.data.Manager
import com.zahar2.andr.data.pack.Pack
import com.zahar2.andr.data.pack.Question
import com.zahar2.andr.data.question.QuestionState
import com.zahar2.andr.data.question.toQuestionSend
import com.zahar2.andr.data.toManager
import io.ktor.server.websocket.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

@OptIn(ExperimentalSerializationApi::class)
class ManagerConnection(
    private val gameStateConnection: GameStateConnection,
    private val questionConnection: QuestionConnection,
    private val videoConnection: VideoConnection
): Connection<Manager>() {

    private val pack: Pack
    private val states: List<Game>

    private var position = 0

    init {
        val res = this::class.java.classLoader.getResource("pack.json")!!
        pack = Json.decodeFromStream(res.openStream())

        val _states = mutableListOf(Game.BeforeStart, Game.StartGame, Game.Rules)

        for ((rIndex, round) in pack.rounds.withIndex()) {
            _states.add(Game.NewRound(rIndex + 1))
            val roundName = round.name
            val total = round.questions.size

            for ((qIndex, question) in round.questions.withIndex()) {
                val num = qIndex + 1
                _states.add(Game.QuestionGame(
                    question.toQuestionSend(QuestionState.INVISIBLE, roundName, num, total)
                ))
                _states.add(Game.QuestionGame(
                    question.toQuestionSend(QuestionState.ONLY_QUESTION, roundName, num, total)
                ))
                _states.add(Game.QuestionGame(
                    question.toQuestionSend(QuestionState.ANSWER, roundName, num, total)
                ))
            }
        }

        _states.add(Game.Winner)

        states = _states
    }

    override suspend fun onNewConnection(session: DefaultWebSocketServerSession) {

    }

    override fun String.toType(): Manager = this.toManager()

    override suspend fun update(data: Manager) {
        if (data == Manager.NEXT) next() else back()
    }

    suspend fun next() {
        if (position < states.size - 1) {
            position++
            invokeState()
        }
    }

    suspend fun back() {
        if (position > 0) {
            position--
            invokeState()
        }
    }

    private suspend fun invokeState() {
        states[position].invoke(
            gameStateConnection,
            questionConnection,
            videoConnection
        )
    }
}