package dev.bugstitch.socionect.modules

import dev.bugstitch.socionect.di.KoinModule
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun Application.module() {

    install(ContentNegotiation){
        json()
    }

    install(Koin){
        SLF4JLogger()
        modules(KoinModule)
    }

    authenticationModule()

    configureDatabase(get())

    routing()
}