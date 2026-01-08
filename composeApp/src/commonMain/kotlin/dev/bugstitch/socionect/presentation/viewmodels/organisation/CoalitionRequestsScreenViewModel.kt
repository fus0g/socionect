package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.CoalitionRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoalitionRequestsScreenViewModel(
    private val coalitionRepository: CoalitionRepository,
    settingsRepository: PreferenceStore
): ViewModel() {

    private val token = settingsRepository.getPreference("access_token")

    private val _list = MutableStateFlow<List<CoalitionRequest>>(emptyList())
    val list: StateFlow<List<CoalitionRequest>> = _list

    suspend fun getRequests(organisation: Organisation)
    {
        if(token != null){
            coalitionRepository.getAllRequests(token,organisation).collect {
                when(it){
                    is NetworkResult.Success -> {
                        _list.value = it.data
                    }
                    else -> {}
                }
            }
        }
    }

    fun acceptRequest(coalitionRequest: CoalitionRequest,organisation: Organisation){
        if(token != null){
            viewModelScope.launch {
                coalitionRepository.acceptRequest(token,coalitionRequest).collect {
                    when(it){
                        is NetworkResult.Success -> {
                            getRequests(organisation)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun declineRequest(coalitionRequest: CoalitionRequest,organisation: Organisation){
        if(token != null){
            viewModelScope.launch {
                coalitionRepository.declineRequest(token,coalitionRequest).collect {
                    when(it){
                        is NetworkResult.Success -> {
                            getRequests(organisation)
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}