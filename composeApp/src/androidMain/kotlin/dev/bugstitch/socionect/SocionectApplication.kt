package dev.bugstitch.socionect

import android.app.Application
import dev.bugstitch.socionect.di.initKoin
import org.koin.android.ext.koin.androidContext

class SocionectApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@SocionectApplication)
        }

    }

}