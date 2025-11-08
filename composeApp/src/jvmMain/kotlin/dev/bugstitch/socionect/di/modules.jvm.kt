package dev.bugstitch.socionect.di

import dev.bugstitch.socionect.data.repository.PreferenceStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module{
        single<HttpClientEngine> {
            OkHttp.create()
        }
        single { PreferenceStore() }
    }