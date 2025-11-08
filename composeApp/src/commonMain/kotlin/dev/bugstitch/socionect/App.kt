package dev.bugstitch.socionect

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.bugstitch.socionect.presentation.navigation.Home
import dev.bugstitch.socionect.presentation.navigation.Login
import dev.bugstitch.socionect.presentation.navigation.Signup
import dev.bugstitch.socionect.presentation.screens.HomeScreen
import dev.bugstitch.socionect.presentation.screens.LoginScreen
import dev.bugstitch.socionect.presentation.screens.SignUpScreen
import dev.bugstitch.socionect.presentation.viewmodels.LoginScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Login) {

            composable<Login> {
                val loginViewModel = koinViewModel<LoginScreenViewModel>()

                val loginSuccess = loginViewModel.loginSuccess.value
                val loading = loginViewModel.loading.value
                val errorMessage = loginViewModel.errorMessage.value

                LaunchedEffect(loginSuccess) {
                    if (loginSuccess) {
                        navController.navigate(Home) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                }

                if (loading && !loginSuccess) {
                    AutoLoginLoadingScreen()
                } else {
                    LoginScreen(
                        email = loginViewModel.email.value,
                        password = loginViewModel.password.value,
                        loading = loading,
                        errorMessage = errorMessage,
                        onEmailChange = { loginViewModel.setEmail(it) },
                        onPasswordChange = { loginViewModel.setPassword(it) },
                        onSignInClick = { loginViewModel.login() },
                        onSignUpClick = { navController.navigate(Signup) }
                    )
                }
            }

            composable<Signup> {
                SignUpScreen()
            }

            composable<Home> {
                HomeScreen()
            }
        }
    }
}

@Composable
fun AutoLoginLoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
            Text("Checking login session...")
        }
    }
}
