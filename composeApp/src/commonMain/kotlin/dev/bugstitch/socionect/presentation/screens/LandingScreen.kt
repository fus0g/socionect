package dev.bugstitch.socionect.presentation.screens

import androidx.compose.runtime.*

@Composable
fun LandingScreen(
    navigateHome: () -> Unit,
    navigateLogin: () -> Unit,
    checking: Boolean,
    autoLoginSuccess: Boolean?
) {

    when {
        checking -> AutoLoginLoadingScreen()

        autoLoginSuccess == true -> {
            LaunchedEffect(Unit) {
                navigateHome()
            }
        }

        autoLoginSuccess == false -> {
            LaunchedEffect(Unit) {
                navigateLogin()
            }
        }
    }
}
