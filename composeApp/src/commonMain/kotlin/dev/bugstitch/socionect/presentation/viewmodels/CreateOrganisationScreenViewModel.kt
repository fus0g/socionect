package dev.bugstitch.socionect.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.launch

class CreateOrganisationScreenViewModel(
    private val organisationRepository: OrganisationRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val _orgName = mutableStateOf("")
    val orgName: MutableState<String> = _orgName

    private val _orgDescription = mutableStateOf("")
    val orgDescription: MutableState<String> = _orgDescription

    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?> = _error

    private val _created = mutableStateOf(false)
    val created: MutableState<Boolean> = _created

    fun setOrgName(value: String) { _orgName.value = value }
    fun setOrgDescription(value: String) { _orgDescription.value = value }

    fun create(){

        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val token = settingsRepository.getPreference("access_token")
            val refreshToken = settingsRepository.getPreference("refresh_token")

            if(token.isNullOrEmpty() || refreshToken.isNullOrEmpty()){
                return@launch
            }

            if(_orgName.value.isBlank() || _orgDescription.value.isBlank()){
                _error.value = "Please fill in all fields"
                return@launch
            }
            organisationRepository.createOrganisation(Organisation(
                id = "",
                name = _orgName.value,
                description = _orgDescription.value,
                createdAt = 0
            ),token).collect {
                when (it) {
                    is NetworkResult.Success ->{
                        _loading.value = false
                        _error.value = null
                        _created.value = true
                    }
                    is NetworkResult.Error ->{
                        _loading.value = false
                        _error.value = it.message
                    }
                    else -> {}
                }
            }
        }

    }

}