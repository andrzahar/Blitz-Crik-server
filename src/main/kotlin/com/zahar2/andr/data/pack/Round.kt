package com.zahar2.andr.data.pack

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Round(
    val name: String,
    val questions: List<Question>
) {
    override fun toString(): String = Json.encodeToString(this)
}

fun String.toRound(): Round = Json.decodeFromString(this)
