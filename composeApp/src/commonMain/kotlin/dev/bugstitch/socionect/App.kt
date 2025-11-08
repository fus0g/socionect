package dev.bugstitch.socionect

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.bugstitch.socionect.presentation.navigation.*
import dev.bugstitch.socionect.presentation.screens.*
import dev.bugstitch.socionect.presentation.viewmodels.*
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Landing) {


            composable<Landing> {
                val viewModel: LandingScreenViewModel = koinViewModel(viewModelStoreOwner = it)

                val checking by viewModel.isChecking.collectAsState()
                val autoLoginSuccess by viewModel.autoLoginSuccess.collectAsState()

                LandingScreen(
                    navigateHome = {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    navigateLogin = {
                        navController.navigate(Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    checking = checking,
                    autoLoginSuccess = autoLoginSuccess
                )
            }

            composable<Login> {
                val loginViewModel = koinViewModel<LoginScreenViewModel>(viewModelStoreOwner = it)


                LaunchedEffect(loginViewModel.loginSuccess.value) {
                    if (loginViewModel.loginSuccess.value) {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                LoginScreen(
                    email = loginViewModel.email.value,
                    password = loginViewModel.password.value,
                    loading = loginViewModel.loading.value,
                    errorMessage = loginViewModel.errorMessage.value,
                    onEmailChange = { loginViewModel.setEmail(it) },
                    onPasswordChange = { loginViewModel.setPassword(it) },
                    onSignInClick = { loginViewModel.login() },
                    onSignUpClick = {
                        navController.navigate(Signup)
                    }
                )
            }


            composable<Signup> {
                val viewModel = koinViewModel<SignUpScreenViewModel>(viewModelStoreOwner = it)

                LaunchedEffect(viewModel.signupSuccess.value) {
                    if (viewModel.signupSuccess.value) {
                        navController.navigate(Home) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                SignUpScreen(
                    name = viewModel.name.value,
                    username = viewModel.username.value,
                    email = viewModel.email.value,
                    password = viewModel.password.value,
                    usernameAvailable = viewModel.usernameAvailable.value,
                    emailAvailable = viewModel.emailAvailable.value,
                    loading = viewModel.loading.value,
                    errorMessage = viewModel.errorMessage.value,
                    onNameChange = { viewModel.setName(it) },
                    onUsernameChange = { viewModel.setUsername(it) },
                    onEmailChange = { viewModel.setEmail(it) },
                    onPasswordChange = { viewModel.setPassword(it) },
                    onSignUpClick = { viewModel.signUp() },
                    onBackToLoginClick = {
                        navController.navigate(Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable<Home> {
                val homeScreenViewModel = koinViewModel<HomeScreenViewModel>(viewModelStoreOwner = it)

                HomeScreen {
                    homeScreenViewModel.logout()
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }
}
