package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrganisationListScreenViewModel(
    private val organisationRepository: OrganisationRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val _organisations = MutableStateFlow(OrganisationListScreenState())
    val organisations: StateFlow<OrganisationListScreenState> = _organisations

    private val token = mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            val token = settingsRepository.getPreference("access_token")
            if(token.isNullOrEmpty()){
                return@launch
            }
            this@OrganisationListScreenViewModel.token.value = token
            fetchAllOrganisations()
        }
    }

    suspend fun fetchAllOrganisations(){
        if(token.value != null)
        {
            _organisations.value = _organisations.value.copy(loading = true)
            organisationRepository.getUserOrganisations(token.value!!).collect {
                when (it) {
                    is NetworkResult.Success -> {
                        _organisations.value = _organisations.value.copy(
                            organisations = it.data,
                            loading = false,
                            error = null
                        )
                    }
                    is NetworkResult.Error -> {
                        _organisations.value = _organisations.value.copy(
                            loading = false,
                            error = it.message
                        )
                    }
                    is NetworkResult.Loading -> {
                        _organisations.value = _organisations.value.copy(
                            loading = true,
                            error = null
                        )
                    }
                }
            }
        }

    }

    data class OrganisationListScreenState(
        val organisations: List<Organisation> = emptyList(),
        val loading: Boolean = false,
        val error:String? = null
    )
}
