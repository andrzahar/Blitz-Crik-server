package com.zahar2.andr.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
enum class Manager {
    BACK, NEXT;

    override fun toString(): String = Json.encodeToString(this)
}

fun String.toManager(): Manager = Json.decodeFromString(this)
