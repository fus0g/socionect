package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.CoalitionRepository
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.CustomLog
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateCoalitionScreenViewModel(
    private val coalitionRepository: CoalitionRepository,
    private val organisationRepository: OrganisationRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val token = settingsRepository.getPreference("access_token")

    private val _name = mutableStateOf("")
    val name: MutableState<String> = _name

    private val _description = mutableStateOf("")
    val description: MutableState<String> = _description

    private val _query = mutableStateOf("")
    val query: MutableState<String> = _query

    private val _addedOrganisations = mutableStateListOf<Organisation>()
    val addedOrganisations: List<Organisation> = _addedOrganisations

    private val _results = MutableStateFlow<List<Organisation>>(emptyList())
    val results: StateFlow<List<Organisation>> = _results

    private val _created = mutableStateOf(false)
    val created: MutableState<Boolean> = _created


    private var searchJob: Job? = null


    fun setName(value: String){
        _name.value = value
    }

    fun setDescription(value: String){
        _description.value = value
    }

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

        organisationRepository.searchOrganisation(query,jwt).collect{ result ->
            when (result) {
                is NetworkResult.Success -> _results.value = result.data
                else -> {}
            }
        }

    }

    fun addOrganisation(organisation: Organisation) {
        if (_addedOrganisations.none { it.id == organisation.id }) {
            _addedOrganisations.add(organisation)
        }
        setQuery(_query.value)
    }


    fun createCoalition(hostOrgId: String){
        if(token != null)
        {
            viewModelScope.launch {
                coalitionRepository.createCoalition(
                    token = token,
                    hostOrg = Organisation(
                        id = hostOrgId,
                        name = "",
                        description = "",
                        createdAt = 0),
                    organisations = _addedOrganisations,
                    name = _name.value,
                    description = _description.value
                    ).collect {
                        when (it) {
                            is NetworkResult.Success -> {
                                _created.value = true
                                CustomLog("CREATED","${_created.value}")
                            }
                            is NetworkResult.Error -> {
                                CustomLog("CREATED","MOBO")
                            }
                            else -> {}
                        }
                }
            }
        }
    }
}