package com.zahar2.andr.connections

import com.zahar2.andr.data.question.QuestionSend
import com.zahar2.andr.data.question.toQuestionSend
import io.ktor.server.websocket.*

class QuestionConnection: Connection<QuestionSend>() {

    private var questionNow = QuestionSend.emptyQuestionSend()

    override fun String.toType(): QuestionSend = this.toQuestionSend()

    override suspend fun onNewConnection(session: DefaultWebSocketServerSession) {
        session.send(questionNow)
    }

    override suspend fun update(data: QuestionSend) { }

    suspend fun change(question: QuestionSend) {
        if (this.questionNow == question) return
        questionNow = question
        broadcast(question)
    }
}