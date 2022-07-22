package com.zahar2.andr.data.question

import com.zahar2.andr.data.pack.Question
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class QuestionSend(
    val author: String,
    val question: String,
    val answer: String,
    val comment: String,
    val questionState: QuestionState,
    val roundName: String,
    val number: Int,
    val total: Int
) {

    companion object {
        fun emptyQuestionSend(): QuestionSend = Question.emptyQuestion().toQuestionSend(QuestionState.INVISIBLE, "",0, 0)
    }

    override fun toString(): String = Json.encodeToString(this)
}

fun String.toQuestionSend(): QuestionSend = Json.decodeFromString(this)

fun Question.toQuestionSend(
    questionState: QuestionState,
    roundName: String,
    number: Int,
    total: Int
): QuestionSend = QuestionSend(
    author,
    question,
    answer,
    comment,
    questionState,
    roundName,
    number,
    total
)
