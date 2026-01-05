package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrowseOrganisationScreenViewModel(
    private val organisationRepository: OrganisationRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val _query = mutableStateOf("")
    val query: MutableState<String> = _query

    private val _results = MutableStateFlow<List<Organisation>>(emptyList())
    val results: StateFlow<List<Organisation>> = _results

    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?> = _error

    private val _state = MutableStateFlow(BrowseOrganisationScreenState())
    val state: StateFlow<BrowseOrganisationScreenState> = _state


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
            getUsersCurrentOrganisation(jwt)
            getUsersRequestedOrganisations(jwt)
        }
    }

    fun sendRequestToOrganisation(organisationId: String){
        viewModelScope.launch {
            val jwt = settingsRepository.getPreference("access_token")
            val refreshToken = settingsRepository.getPreference("refresh_token")
            if (jwt.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                return@launch
            }
            organisationRepository.userSendsRequestToOrganisation(organisationId,jwt).collect {
                when(it){
                    is NetworkResult.Success -> {
                        getUsersCurrentOrganisation(jwt)
                        getUsersRequestedOrganisations(jwt)
                    }
                    else -> {
                        getUsersCurrentOrganisation(jwt)
                        getUsersRequestedOrganisations(jwt)
                    }
                }
            }

        }
    }



    private suspend fun performSearch(jwt: String, query: String) {
        _loading.value = true
        _error.value = null


        organisationRepository.searchOrganisation(query,jwt).collect { result ->
            when (result) {
                is NetworkResult.Success -> _results.value = result.data
                is NetworkResult.Error -> _error.value = result.message
                else -> {}
            }
        }

        _loading.value = false
    }

    private suspend fun getUsersCurrentOrganisation(jwt: String){
        organisationRepository.getUserOrganisations(jwt).collect {
            when(it){
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        usersCurrentOrganisation = it.data
                    )
                }
                else -> {}
            }
        }
    }

    private suspend fun getUsersRequestedOrganisations(jwt: String){
        organisationRepository.getAllRequestsSendByUserToOrg(jwt).collect {
            when(it){
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        usersRequestedOrganisations = it.data
                    )
                }
                else -> {}
            }
        }
    }

    data class BrowseOrganisationScreenState(
        val usersCurrentOrganisation: List<Organisation> = emptyList(),
        val usersRequestedOrganisations: List<Organisation> = emptyList()
    )

}