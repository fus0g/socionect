package dev.bugstitch.socionect

import dev.bugstitch.socionect.modules.module
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}