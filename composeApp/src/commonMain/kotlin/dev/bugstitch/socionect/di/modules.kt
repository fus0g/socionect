package dev.bugstitch.socionect.di

import dev.bugstitch.socionect.data.repository.UserRepositoryImpl
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.presentation.viewmodels.LoginScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.SignUpScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
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

    single<UserRepository> { UserRepositoryImpl(get()) }

    singleOf(::LoginScreenViewModel)
    singleOf(::SignUpScreenViewModel)

}