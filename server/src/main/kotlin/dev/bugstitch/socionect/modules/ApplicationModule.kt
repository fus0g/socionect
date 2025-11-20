package dev.bugstitch.socionect.modules

import dev.bugstitch.socionect.di.KoinModule
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.websocket.WebSockets
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun Application.module() {

    val jsonConfig = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    install(ContentNegotiation){
        json(jsonConfig)
    }

    install(CORS)
    {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowCredentials = true
    }

    install(Koin){
        SLF4JLogger()
        modules(KoinModule)
    }

    install(WebSockets){
        pingPeriodMillis = 1000
        contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
    }

    authenticationModule()

    configureDatabase(get())

    routing()
}