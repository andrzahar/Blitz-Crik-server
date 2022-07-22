package com.zahar2.andr.data.pack

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Question(
    val author: String,
    val question: String,
    val answer: String,
    val comment: String
) {
    companion object {
        fun emptyQuestion() = Question("", "", "", "")
    }

    override fun toString(): String = Json.encodeToString(this)
}

fun String.toQuestion(): Question = Json.decodeFromString(this)
