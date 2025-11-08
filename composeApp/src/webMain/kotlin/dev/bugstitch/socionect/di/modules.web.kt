package dev.bugstitch.socionect.di

import dev.bugstitch.socionect.data.repository.PreferenceStore
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.module.Module
import org.koin.dsl.module
import io.ktor.client.engine.js.Js

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> {
            Js.create()
        }
        single { PreferenceStore() }
    }