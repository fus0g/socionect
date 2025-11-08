package dev.bugstitch.socionect.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val settingsRepository: PreferenceStore
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            settingsRepository.removePreference("access_token")
            settingsRepository.removePreference("refresh_token")
        }
    }
}
