package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.launch

class CreateOrganisationSubtopicScreenViewModel(
    private val organisationSubtopicRepository: OrganisationSubtopicRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val token = mutableStateOf<String?>(null)

    private val _subtopicName = mutableStateOf("")
    val subtopicName: MutableState<String> = _subtopicName

    private val _subtopicDescription = mutableStateOf("")
    val subtopicDescription: MutableState<String> = _subtopicDescription

    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?> = _error

    private val _finished = mutableStateOf(false)
    val finished: MutableState<Boolean> = _finished


    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean> = _loading

    init {
        token.value = settingsRepository.getPreference("access_token")
    }

    fun setSubtopicName(value: String) { _subtopicName.value = value }
    fun setSubtopicDescription(value: String) { _subtopicDescription.value = value }

    fun createSubtopic(organisationId: String){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            organisationSubtopicRepository.createSubtopic(
                OrganisationSubtopic(
                    id = "",
                    name = _subtopicName.value,
                    description = _subtopicDescription.value,
                    organisationId = organisationId,
                    createdAt = 0
                ),
                token = token.value!!
            ).collect{
                when(it){
                    is NetworkResult.Success ->{
                        _loading.value = false
                        _error.value = null
                        _finished.value = true
                        }
                    is NetworkResult.Error ->{
                        _loading.value = false
                        _error.value = it.message
                    }
                    is NetworkResult.Loading -> {
                        _loading.value = true
                        _error.value = null
                        _finished.value = false
                    }
                }
            }
        }
    }


}