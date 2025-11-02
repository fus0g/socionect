package dev.bugstitch.socionect.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<HttpClient> { HttpClient(get<HttpClientEngine>()){
        install(ContentNegotiation){
            json(json = Json{
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
        }
    }
}