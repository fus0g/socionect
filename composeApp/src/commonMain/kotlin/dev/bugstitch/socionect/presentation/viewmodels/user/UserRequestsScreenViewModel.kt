package dev.bugstitch.socionect.presentation.viewmodels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.CustomLog
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRequestsScreenViewModel(
    private val organisationRepository: OrganisationRepository,
    private val settingsRepository: PreferenceStore
)
    : ViewModel() {


    private val token = settingsRepository.getPreference("access_token")

    private val _state = MutableStateFlow(UserRequestsScreenState())
    val state: StateFlow<UserRequestsScreenState> = _state

    suspend fun getReceivedRequests() {
        if(token == null) return

        organisationRepository.getRequestsReceivedByUser(token).collect {
            when(it)
            {
                is NetworkResult.Success ->{
                    _state.value = _state.value.copy(
                        receivedRequests = it.data
                    )
                    CustomLog("Received",it.data.toString())
                }
                else -> {}
            }
        }
    }

    suspend fun getSentRequests() {
        if(token == null) return

        organisationRepository.getAllRequestsSendByUserToOrg(token).collect {
            when(it)
            {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        sentRequests = it.data
                    )
                    CustomLog("Sent",it.data.toString())
                }
                else -> {}
            }
        }

    }

    fun confirmRequest(organisation: Organisation){
        if(token == null) return

        viewModelScope.launch {
            organisationRepository.userAcceptsRequest(organisation.id,token).collect {
                when(it)
                {
                    is NetworkResult.Success -> {
                        getReceivedRequests()
                        getSentRequests()
                    }
                    else -> {}
                }
            }
        }
    }

    fun declineRequest(organisation: Organisation){
        if(token == null) return

        viewModelScope.launch {
            organisationRepository.userDeclinesRequest(organisation.id,token).collect {
                when(it)
                {
                    is NetworkResult.Success -> {
                        getReceivedRequests()
                        getSentRequests()
                    }
                    else -> {}
                }
            }
        }
    }




    data class UserRequestsScreenState(
        val receivedRequests: List<Organisation> = emptyList(),
        val sentRequests: List<Organisation> = emptyList()
    )

}