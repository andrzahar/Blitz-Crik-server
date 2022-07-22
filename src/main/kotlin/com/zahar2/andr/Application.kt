package com.zahar2.andr

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.zahar2.andr.plugins.*

fun main() {
    embeddedServer(Netty, port = 2207, host = "0.0.0.0") {
        configureSockets()
    }.start(wait = true)
}
