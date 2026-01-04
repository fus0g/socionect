package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrganisationMainScreenViewModel(
    private val organisationRepository: OrganisationRepository,
    private val organisationSubtopicRepository: OrganisationSubtopicRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val token = mutableStateOf<String?>(null)

    private val _state = MutableStateFlow(OrganisationMainScreenState())
    val state: StateFlow<OrganisationMainScreenState> = _state

    init {
        token.value = settingsRepository.getPreference("access_token")
    }


    fun fetchSubtopics(organisationId: String){
        if(token.value != null){
            viewModelScope.launch {
                _state.value = _state.value.copy(loading = true)
                organisationSubtopicRepository.getAllOrganisationSubTopic(organisationId,token.value!!).collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                subtopics = it.data,
                                loading = false,
                                error = null
                            )
                        }
                        is NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                error = it.message
                            )
                        }
                        is NetworkResult.Loading -> {}
                    }
                }
            }
        }
    }



    data class OrganisationMainScreenState(
        val subtopics: List<OrganisationSubtopic> = emptyList(),
        val loading: Boolean = false,
        val error:String? = null
    )
}


