package dev.bugstitch.socionect

import androidx.compose.ui.window.ComposeUIViewController
import dev.bugstitch.socionect.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}