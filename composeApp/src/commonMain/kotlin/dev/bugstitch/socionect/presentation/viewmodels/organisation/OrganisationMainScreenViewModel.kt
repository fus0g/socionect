package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.repository.CoalitionRepository
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrganisationMainScreenViewModel(
    private val organisationRepository: OrganisationRepository,
    private val organisationSubtopicRepository: OrganisationSubtopicRepository,
    private val coalitionRepository: CoalitionRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val token = mutableStateOf<String?>(null)

    private val _state = MutableStateFlow(OrganisationMainScreenState())
    val state: StateFlow<OrganisationMainScreenState> = _state

    init {
        token.value = settingsRepository.getPreference("access_token")
    }




    fun fetchSubtopics(organisationId: String) {
        val jwt = token.value ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)

            organisationSubtopicRepository
                .getAllOrganisationSubTopic(organisationId, jwt)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val uniqueSubtopics = result.data
                                .distinctBy { it.id }

                            _state.value = _state.value.copy(
                                subtopics = uniqueSubtopics,
                                loading = false,
                                error = null
                            )
                        }

                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                error = result.message
                            )
                        }

                        else -> {}
                    }
                }
        }
    }


    fun fetchCoalitions(organisationId: String) {
        val jwt = token.value ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)

            coalitionRepository
                .getAllCoalitions(
                    jwt,
                    Organisation(
                        id = organisationId,
                        name = "",
                        description = "",
                        createdAt = 0
                    )
                )
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            val uniqueCoalitions = result.data
                                .distinctBy { it.id }

                            _state.value = _state.value.copy(
                                coalitions = uniqueCoalitions,
                                loading = false,
                                error = null
                            )
                        }

                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                error = result.message
                            )
                        }

                        else -> {}
                    }
                }
        }
    }


    data class OrganisationMainScreenState(
        val subtopics: List<OrganisationSubtopic> = emptyList(),
        val coalitions: List<Coalition> = emptyList(),
        val loading: Boolean = false,
        val error:String? = null
    )
}


