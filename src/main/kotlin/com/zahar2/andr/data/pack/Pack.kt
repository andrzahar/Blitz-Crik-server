package com.zahar2.andr.data.pack

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Pack(
    val author: String,
    val smart: String,
    val date: String,
    val rounds: List<Round>
) {
    override fun toString(): String = Json.encodeToString(this)
}

fun String.toPack(): Pack = Json.decodeFromString(this)
