package dev.bugstitch.socionect.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserSearchViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: PreferenceStore
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _results = MutableStateFlow<List<User>>(emptyList())
    val results: StateFlow<List<User>> = _results

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var searchJob: Job? = null

    fun setQuery(q: String) {
        _query.value = q

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            if (q.isBlank()) {
                _results.value = emptyList()
                return@launch
            }
            val jwt = settingsRepository.getPreference("access_token")
            val refreshToken = settingsRepository.getPreference("refresh_token")

            if (jwt.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                return@launch
            }

            performSearch(jwt,q)
        }
    }

    private suspend fun performSearch(jwt: String, query: String) {
        _loading.value = true
        _error.value = null


        userRepository.searchUsers(jwt,query).collect { result ->
            when (result) {
                is NetworkResult.Success -> _results.value = result.data ?: emptyList()
                is NetworkResult.Error -> _error.value = result.message
                else -> {}
            }
        }

        _loading.value = false
    }
}
