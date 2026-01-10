package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.CoalitionRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
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

    private var loadJob: Job? = null
    fun loadRequests(organisation: Organisation) {
        if (token == null) return

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            coalitionRepository
                .getAllRequests(token, organisation)
                .collect { result ->
                    if (result is NetworkResult.Success) {
                        _list.value = result.data.toList()
                    }
                }
        }
    }



    fun acceptRequest(req: CoalitionRequest, org: Organisation) {
        _list.value = _list.value.filterNot { it.id == req.id }

        viewModelScope.launch {
            coalitionRepository.acceptRequest(token!!, req).collect {
                if (it is NetworkResult.Success) {
                    loadRequests(org)
                }
            }
        }
    }


    fun declineRequest(coalitionRequest: CoalitionRequest,organisation: Organisation){
        _list.value = _list.value.filterNot { it.id == coalitionRequest.id }
        if(token != null){
            viewModelScope.launch {
                coalitionRepository.declineRequest(token,coalitionRequest).collect {
                    when(it){
                        is NetworkResult.Success -> {
                            loadRequests(organisation)
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}