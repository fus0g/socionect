package dev.bugstitch.socionect.presentation.screens

import androidx.compose.runtime.*
import androidx.navigation.NavController
import dev.bugstitch.socionect.presentation.viewmodels.LandingScreenViewModel
import org.koin.compose.viewmodel.koinViewModel
import dev.bugstitch.socionect.presentation.navigation.Home
import dev.bugstitch.socionect.presentation.navigation.Login

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
