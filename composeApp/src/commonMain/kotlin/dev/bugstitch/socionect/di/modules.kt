package dev.bugstitch.socionect.di

import dev.bugstitch.socionect.data.repository.OneToOneChatRepositoryImpl
import dev.bugstitch.socionect.data.repository.OrganisationRepositoryImpl
import dev.bugstitch.socionect.data.repository.OrganisationSubtopicRepositoryImpl
import dev.bugstitch.socionect.data.repository.UserRepositoryImpl
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.presentation.viewmodels.ChatRoomViewModel
import dev.bugstitch.socionect.presentation.viewmodels.CreateOrganisationScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.HomeScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.LandingScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.LoginScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationListScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.SignUpScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.UserSearchViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.CreateOrganisationSubtopicScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.organisation.OrganisationMainScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<HttpClient> {
        val jsonConfig = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
        }

        HttpClient(get<HttpClientEngine>()) {
            install(ContentNegotiation) {
                json(jsonConfig)
            }

            install(WebSockets) {
                pingIntervalMillis = 1_000
                contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
            }
        }
    }

    single<UserRepository> { UserRepositoryImpl(get()) }

    single<OneToOneChatRepository> { OneToOneChatRepositoryImpl(get()) }

    single<OrganisationRepository> { OrganisationRepositoryImpl(get()) }

    single<OrganisationSubtopicRepository> { OrganisationSubtopicRepositoryImpl(get()) }


    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::SignUpScreenViewModel)
    viewModelOf(::LandingScreenViewModel)
    viewModelOf(::UserSearchViewModel)
    viewModelOf(::ChatRoomViewModel)
    viewModelOf(::CreateOrganisationScreenViewModel)
    viewModelOf(::OrganisationListScreenViewModel)
    viewModelOf(::OrganisationMainScreenViewModel)
    viewModelOf(::CreateOrganisationSubtopicScreenViewModel)

    singleOf(::HomeScreenViewModel)
}