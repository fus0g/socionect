package dev.bugstitch.socionect.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LandingScreenViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: PreferenceStore
) : ViewModel() {

    private val _isChecking = MutableStateFlow(true)
    val isChecking: StateFlow<Boolean> = _isChecking

    private val _autoLoginSuccess = MutableStateFlow<Boolean?>(null)
    val autoLoginSuccess: StateFlow<Boolean?> = _autoLoginSuccess

    init {
        tryAutoLogin()
    }

    private fun tryAutoLogin() {
        viewModelScope.launch {
            val jwt = settingsRepository.getPreference("access_token")
            val refreshToken = settingsRepository.getPreference("refresh_token")

            if (jwt.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                _autoLoginSuccess.value = false
                _isChecking.value = false
                return@launch
            }

            userRepository.helloUser(jwt).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        _autoLoginSuccess.value = true
                        _isChecking.value = false
                    }

                    is NetworkResult.Error -> {
                        if (result.message?.contains("Unauthorized") == true) {
                            tryRefresh(refreshToken)
                        } else {
                            _autoLoginSuccess.value = false
                            _isChecking.value = false
                        }
                    }

                    else -> {}
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
                        settingsRepository.setPreference("access_token", newTokens.token)
                        settingsRepository.setPreference("refresh_token", newTokens.refreshToken)
                        _autoLoginSuccess.value = true
                    } else {
                        _autoLoginSuccess.value = false
                    }
                }

                else -> _autoLoginSuccess.value = false
            }
        }
        _isChecking.value = false
    }
}
