package dev.bugstitch.socionect.presentation.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.data.models.TokenDTO
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: PreferenceStore
) : ViewModel() {

    // UI State
    private val _email = mutableStateOf("")
    val email: MutableState<String> = _email

    private val _password = mutableStateOf("")
    val password: MutableState<String> = _password

    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean> = _loading

    private val _loginSuccess = mutableStateOf(false)
    val loginSuccess: MutableState<Boolean> = _loginSuccess

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: MutableState<String?> = _errorMessage

    // No init block

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun login() {
        viewModelScope.launch(Dispatchers.Unconfined) {
            _loading.value = true
            _errorMessage.value = null

            val user = User(email = _email.value, password = _password.value)

            userRepository.login(user).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val tokens = result.data
                        saveTokens(tokens)
                        _loginSuccess.value = true
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

    private fun saveTokens(tokens: TokenDTO) {
        settingsRepository.setPreference("access_token", tokens.token)
        settingsRepository.setPreference("refresh_token", tokens.refreshToken)
    }
}