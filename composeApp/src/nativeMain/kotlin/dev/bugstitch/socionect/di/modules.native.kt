package dev.bugstitch.socionect.di

import dev.bugstitch.socionect.data.repository.PreferenceStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformModule: org.koin.core.module.Module
    get() = module {
        single<HttpClientEngine> {
            Darwin.create()
        }
        single { PreferenceStore() }
    }