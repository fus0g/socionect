package dev.bugstitch.socionect

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.bugstitch.socionect.di.initKoin
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.painterResource
import socionect.composeapp.generated.resources.Res
import socionect.composeapp.generated.resources.logo

fun main() = application {
    initKoin()

    val state = rememberWindowState(
        width = 1280.dp,
        height = 720.dp
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Socionect",
        icon = painterResource(Res.drawable.logo),
        state = state
    ) {
        App()
    }
}