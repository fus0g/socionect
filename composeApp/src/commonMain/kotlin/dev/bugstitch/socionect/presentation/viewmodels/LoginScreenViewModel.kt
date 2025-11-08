package dev.bugstitch.socionect.presentation.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.data.models.TokenDTO
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: PreferenceStore
) : ViewModel() {

    // UI State
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: State<Boolean> = _loginSuccess

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    init {
        viewModelScope.launch {
            val str = settingsRepository.getPreference("access_token")
            if(!str.isNullOrEmpty())
            {
                _email.value = str
            }
            tryAutoLogin()
        }
    }

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun login() {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null

            val user = User(email = _email.value, password = _password.value)

            userRepository.login(user).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val tokens = result.data
                        if (tokens != null) {
                            saveTokens(tokens)
                            _loginSuccess.value = true
                        }
                    }

                    is NetworkResult.Error -> {
                        _errorMessage.value = result.message
                        _loginSuccess.value = false
                    }

                    else -> {}
                }
            }

            _loading.value = false
        }
    }

    private suspend fun saveTokens(tokens: TokenDTO) {
        settingsRepository.setPreference("access_token", tokens.token)
        settingsRepository.setPreference("refresh_token", tokens.refreshToken)
    }

    private suspend fun tryAutoLogin() {
        val jwt = settingsRepository.getPreference("access_token")
        val refreshToken = settingsRepository.getPreference("refresh_token")


        if (jwt.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            return
        }

        _loading.value = true

        userRepository.helloUser(jwt).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    _loginSuccess.value = true
                    _email.value = "Hello"
                    _loading.value = false
                    return@collect
                }

                is NetworkResult.Error -> {
                    if (result.message?.contains("Unauthorized") == true) {
                        tryRefresh(refreshToken)
                        _email.value = "Lello"

                    } else {
                        _loginSuccess.value = false
                        _loading.value = false
                        _email.value = "Kello"
                    }
                }

                else -> {
                    _email.value = "Mello"
                }
            }
        }
    }

    private suspend fun tryRefresh(refreshToken: String) {
        userRepository.refreshToken(refreshToken).collect { refreshResult ->
            when (refreshResult) {
                is NetworkResult.Success -> {
                    val newTokens = refreshResult.data
                    if (newTokens != null) {
                        saveTokens(newTokens)

                        userRepository.helloUser(newTokens.token).collect { helloResult ->
                            when (helloResult) {
                                is NetworkResult.Success -> {
                                    _loginSuccess.value = true
                                }

                                is NetworkResult.Error -> {
                                    _loginSuccess.value = false
                                    _errorMessage.value = "Session expired. Please login again."
                                }

                                else -> {}
                            }
                        }
                    }
                }

                is NetworkResult.Error -> {
                    _loginSuccess.value = false
                    _errorMessage.value = "Refresh failed. Please login again."
                }

                else -> {}
            }
        }

        _loading.value = false
    }
}
