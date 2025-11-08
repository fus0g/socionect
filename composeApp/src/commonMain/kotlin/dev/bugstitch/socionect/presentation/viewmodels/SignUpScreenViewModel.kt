package dev.bugstitch.socionect.presentation.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.models.TokenDTO
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpScreenViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: PreferenceStore
) : ViewModel() {

    // Fields
    private val _name = mutableStateOf("")
    val name: MutableState<String> = _name

    private val _username = mutableStateOf("")
    val username: MutableState<String> = _username

    private val _email = mutableStateOf("")
    val email: MutableState<String> = _email

    private val _password = mutableStateOf("")
    val password: MutableState<String> = _password

    // UI States
    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean> = _loading

    private val _signupSuccess = mutableStateOf(false)
    val signupSuccess: MutableState<Boolean> = _signupSuccess

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: MutableState<String?> = _errorMessage

    // Availability checks
    private val _usernameAvailable = mutableStateOf<Boolean?>(null)
    val usernameAvailable: MutableState<Boolean?> = _usernameAvailable

    private val _emailAvailable = mutableStateOf<Boolean?>(null)
    val emailAvailable: MutableState<Boolean?> = _emailAvailable

    // Typing debounce jobs
    private var usernameJob: Job? = null
    private var emailJob: Job? = null

    fun setName(value: String) { _name.value = value }

    fun setUsername(value: String) {
        _username.value = value
        usernameJob?.cancel()
        usernameJob = viewModelScope.launch {
            delay(500)
            checkUsernameAvailability(value)
        }
    }

    fun setEmail(value: String) {
        _email.value = value
        emailJob?.cancel()
        emailJob = viewModelScope.launch {
            delay(500)
            checkEmailAvailability(value)
        }
    }

    fun setPassword(value: String) { _password.value = value }

    private suspend fun checkUsernameAvailability(username: String) {
        if (username.isBlank()) {
            _usernameAvailable.value = null
            return
        }

        val user = User(username = username)
        userRepository.checkUserNameConflict(user).collect { result ->
            when (result) {
                is NetworkResult.Success -> _usernameAvailable.value = !result.data!!
                is NetworkResult.Error -> _usernameAvailable.value = null
                else -> {}
            }
        }
    }

    private suspend fun checkEmailAvailability(email: String) {
        if (email.isBlank()) {
            _emailAvailable.value = null
            return
        }

        val user = User(email = email)
        userRepository.checkEmailConflict(user).collect { result ->
            when (result) {
                is NetworkResult.Success -> _emailAvailable.value = !result.data!!
                is NetworkResult.Error -> _emailAvailable.value = null
                else -> {}
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null

            val user = User(
                name = _name.value,
                username = _username.value,
                email = _email.value,
                password = _password.value
            )

            userRepository.signUp(user).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val tokens = result.data
                        if (tokens != null) {
                            viewModelScope.launch {
                                saveTokens(tokens)
                                _signupSuccess.value = true
                            }
                        } else {
                            _errorMessage.value = "No token received"
                        }
                    }

                    is NetworkResult.Error -> {
                        _errorMessage.value = result.message ?: "Signup failed"
                        _signupSuccess.value = false
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
