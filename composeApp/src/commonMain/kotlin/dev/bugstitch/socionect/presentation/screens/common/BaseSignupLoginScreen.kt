package dev.bugstitch.socionect.presentation.screens.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import dev.bugstitch.socionect.presentation.components.Logo
import dev.bugstitch.socionect.presentation.viewmodels.LoginScreenViewModel
import dev.bugstitch.socionect.presentation.viewmodels.SignUpScreenViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BaseSignupLoginScreen(
    onLoginSuccess: () -> Unit,
    onSignupSuccess: () -> Unit,
    isLarge: Boolean
){

    val page = rememberSaveable{mutableStateOf(0)}
    val pagerState = rememberPagerState(
        initialPage = page.value,
        pageCount = {2}
    )

    val scope = rememberCoroutineScope()

    Row(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.sizeIn(maxWidth = 600.dp)) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ){ page->
                when(page){
                    0 ->{
                        val loginScreenVM = koinViewModel<LoginScreenViewModel>()

                        LaunchedEffect(loginScreenVM.loginSuccess.value) {
                            if (loginScreenVM.loginSuccess.value) {
                                onLoginSuccess()
                            }
                        }

                        LoginScreen(
                            email = loginScreenVM.email.value,
                            password = loginScreenVM.password.value,
                            loading = loginScreenVM.loading.value,
                            errorMessage = loginScreenVM.errorMessage.value,
                            onEmailChange = { loginScreenVM.setEmail(it) },
                            onPasswordChange = { loginScreenVM.setPassword(it) },
                            onSignInClick = { loginScreenVM.login() },
                            onSignUpClick = {
                                scope.launch {
                                    pagerState.scrollToPage(1)
                                }
                            },
                            isLarge = isLarge
                        )
                    }

                    1 ->{
                        val signupScreenVM = koinViewModel<SignUpScreenViewModel>()

                        LaunchedEffect(signupScreenVM.signupSuccess.value) {
                            if (signupScreenVM.signupSuccess.value) {
                                onSignupSuccess()
                            }
                        }

                        SignUpScreen(
                            name = signupScreenVM.name.value,
                            username = signupScreenVM.username.value,
                            email = signupScreenVM.email.value,
                            password = signupScreenVM.password.value,
                            usernameAvailable = signupScreenVM.usernameAvailable.value,
                            emailAvailable = signupScreenVM.emailAvailable.value,
                            loading = signupScreenVM.loading.value,
                            errorMessage = signupScreenVM.errorMessage.value,
                            onNameChange = { signupScreenVM.setName(it) },
                            onUsernameChange = { signupScreenVM.setUsername(it) },
                            onEmailChange = { signupScreenVM.setEmail(it) },
                            onPasswordChange = { signupScreenVM.setPassword(it) },
                            onSignUpClick = { signupScreenVM.signUp() },
                            onBackToLoginClick = {
                                scope.launch {
                                    pagerState.scrollToPage(0)
                                }
                            },
                            isLarge = isLarge
                        )
                    }
                }
            }
        }

        if(isLarge){
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Logo(100,100)
            }
        }
    }
}