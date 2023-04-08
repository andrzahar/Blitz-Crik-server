package com.zahar2.andr.data

import com.zahar2.andr.connections.GameStateConnection
import com.zahar2.andr.connections.QuestionConnection
import com.zahar2.andr.connections.VideoConnection
import com.zahar2.andr.data.pack.Question
import com.zahar2.andr.data.question.QuestionSend

sealed class Game {

    protected lateinit var gameStateConnection: GameStateConnection
    protected lateinit var questionConnection: QuestionConnection
    protected lateinit var videoConnection: VideoConnection

    object BeforeStart : Game() {
        override suspend fun invoke() = change(GameState.BEFORE_START, QuestionSend.emptyQuestionSend(), Video.NONE)
    }

    object StartGame : Game() {
        override suspend fun invoke() = change(GameState.SPLASH_SCREEN, QuestionSend.emptyQuestionSend(), Video.START)
    }

    object Introduction : Game() {
        override suspend fun invoke() = change(GameState.SPLASH_SCREEN, QuestionSend.emptyQuestionSend(), Video.NONE)
    }

    object Rules : Game() {
        override suspend fun invoke() = change(GameState.RULES, QuestionSend.emptyQuestionSend(), Video.NONE)
    }

    class NewRound(private val number: Int) : Game() {
        override suspend fun invoke() = change(
            GameState.GAME,
            QuestionSend.emptyQuestionSend(),
            when (number) {
                1 -> Video.ROUND_1
                2 -> Video.ROUND_2
                3 -> Video.ROUND_3
                else -> Video.NONE
            }
        )
    }

    class QuestionGame(private val questionSend: QuestionSend) : Game() {
        override suspend fun invoke() = change(GameState.GAME, questionSend, Video.NONE)
    }

    object Winner : Game() {
        override suspend fun invoke() = change(GameState.WINNER, QuestionSend.emptyQuestionSend(), Video.NONE)
    }

    protected abstract suspend operator fun invoke()

    protected suspend fun change(gameState: GameState, question: QuestionSend, video: Video) {
        gameStateConnection.change(gameState)
        questionConnection.change(question)
        videoConnection.change(video)
    }

    suspend operator fun invoke(
        gameStateConnection: GameStateConnection,
        questionConnection: QuestionConnection,
        videoConnection: VideoConnection
    ) {
        this.gameStateConnection = gameStateConnection
        this.questionConnection = questionConnection
        this.videoConnection = videoConnection

        invoke()
    }
}
