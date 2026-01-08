package dev.bugstitch.socionect.presentation.viewmodels.organisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import dev.bugstitch.socionect.domain.repository.OrganisationChatRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrganisationSubtopicChatViewModel(
    private val organisationChatRepository: OrganisationChatRepository,
    private val settingsRepository: PreferenceStore
): ViewModel() {

    private val _messages = MutableStateFlow<List<OrganisationSubtopicMessage>>(emptyList())
    val messages: StateFlow<List<OrganisationSubtopicMessage>> = _messages
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var isConnected = false

    private var listeningJob: Job? = null

    fun loadChat(subtopicId: String) {
        val token = settingsRepository.getPreference("access_token") ?: return

        viewModelScope.launch {
            organisationChatRepository.connectSubtopicChat(subtopicId, token).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> _loading.value = true
                    is NetworkResult.Success -> {
                        _loading.value = false
                        if (result.data == true) {
                            isConnected = true
                            startListening(subtopicId, token)
                        }
                    }
                    is NetworkResult.Error -> {
                        _loading.value = false
                        isConnected = false
                        println("Connection Error: ${result.message}")
                    }
                }
            }
        }
    }

    private fun startListening(subtopicId: String, token: String) {
        listeningJob?.cancel()
        listeningJob = viewModelScope.launch {
            organisationChatRepository.incomingSubtopicMessages(subtopicId, token).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val newMessages = result.data
                        _messages.update { current -> current + newMessages }
                    }
                    is NetworkResult.Error -> {
                        println("Receive Error: ${result.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    fun sendMessage(text: String, subtopicId: String) {
        if (!isConnected || text.isBlank()) return

        val token = settingsRepository.getPreference("access_token") ?: return

        viewModelScope.launch {
            organisationChatRepository.sendSubtopicMessage(text, subtopicId, token).collect { result ->
                if (result is NetworkResult.Error) {
                    println("Send failed: ${result.message}")
                }
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            organisationChatRepository.detach()
        }
        super.onCleared()
    }

}