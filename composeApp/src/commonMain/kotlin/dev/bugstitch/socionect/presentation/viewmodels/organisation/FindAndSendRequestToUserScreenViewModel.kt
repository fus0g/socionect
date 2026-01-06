package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FindAndSendRequestToUserScreenViewModel(
    private val userRepository: UserRepository,
    private val organisationRepository: OrganisationRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val token = settingsRepository.getPreference("access_token")

    private val _state = MutableStateFlow(FindAndSendRequestToUserScreenState())
    val state: MutableStateFlow<FindAndSendRequestToUserScreenState> = _state

    private val _query = mutableStateOf("")
    val query: MutableState<String> = _query

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: MutableStateFlow<String?> = _error

    private var searchJob: Job? = null

    fun setQuery(value: String) {
        _query.value = value
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _loading.value = true
            delay(400)
            if (_query.value.isBlank()) {
                return@launch
            }
            searchUsers()
        }
    }

    private suspend fun searchUsers(){
        if(token == null) return
        if(_query.value.isBlank()) return
        userRepository.searchUsers(token, _query.value).collect{
            when(it){
                is NetworkResult.Success ->{
                    _loading.value = false

                    _state.update { state ->
                        state.copy(
                            searchedUsers = it.data
                        )
                    }
                }
                else -> {}
            }
        }
    }

    suspend fun getMemberUsers(organisationId: String){
        if(token == null) return
        organisationRepository.getAllMembers(organisationId,token).collect {
            when(it){
                is NetworkResult.Success ->{
                    _state.update { state ->
                        state.copy(
                            memberUsers = it.data
                        )
                    }
                }
                else -> {}
            }
        }
    }

    suspend fun getRequestedUsers(organisationId: String) {
        if (token == null) return
        organisationRepository.getRequestsSendToUser(organisationId,token).collect {
            when (it) {
                is NetworkResult.Success -> {
                    _state.update { state ->
                        state.copy(
                            requestedUsers = it.data
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun sendRequestToUser(userId: String,organisationId: String) {
        if (token == null) return
        viewModelScope.launch {
            organisationRepository.sendRequestToUser(userId,organisationId,token).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        getRequestedUsers(organisationId)
                        getMemberUsers(organisationId)
                        searchUsers()
                    }
                    else -> {}
                }
            }
        }
    }


    data class FindAndSendRequestToUserScreenState(
        val searchedUsers: List<User> = emptyList(),
        val requestedUsers: List<User> = emptyList(),
        val memberUsers: List<User> = emptyList()
    )

}