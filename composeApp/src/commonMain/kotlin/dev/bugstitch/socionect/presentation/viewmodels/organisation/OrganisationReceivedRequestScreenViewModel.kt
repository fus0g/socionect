package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrganisationReceivedRequestScreenViewModel(
    private val organisationRepository: OrganisationRepository,
    private val settingsRepository: PreferenceStore
) : ViewModel() {

    private val _state = MutableStateFlow(LocalState())
    val state: StateFlow<LocalState> = _state


    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?> = _error

    private val _finished = mutableStateOf(false)
    val finished: MutableState<Boolean> = _finished

    private val token = settingsRepository.getPreference("access_token")

    fun getRequests(organisationId: String) {
        viewModelScope.launch {
            if (token == null) return@launch
            organisationRepository.getRequestsReceivedByOrganisation(organisationId, token).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _state.value = _state.value.copy(
                            users = it.data
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun acceptRequest(userId: String, organisationId: String) {
        viewModelScope.launch {
            if (token == null) return@launch
            organisationRepository.organisationAcceptsRequest(
                userId = userId,
                organisationId = organisationId,
                token = token
            ).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _finished.value = true
                    }
                    else -> {}
                }
            }
        }
    }

    fun declineRequest(userId: String, organisationId: String) {
        viewModelScope.launch {
            if (token == null) return@launch
            organisationRepository.organisationDeclinesRequest(
                userId = userId,
                organisationId = organisationId,
                token = token
            ).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _finished.value = true
                    }
                    else -> {}
                }
            }
        }
    }




    data class LocalState(
        val users: List<User> = emptyList()
    )

}