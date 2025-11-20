package dev.bugstitch.socionect.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bugstitch.socionect.data.repository.PreferenceStore
import dev.bugstitch.socionect.domain.models.ChatMessage
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatRoomViewModel(
    private val chatRepository: OneToOneChatRepository,
    private val settings: PreferenceStore
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var isConnected = false

    private var listeningJob: Job? = null

    fun loadChat(otherUserId: String) {
        val token = settings.getPreference("access_token") ?: return

        viewModelScope.launch {
            chatRepository.connect(otherUserId, token).collect { result ->
                when (result) {
                    is NetworkResult.Loading -> _loading.value = true
                    is NetworkResult.Success -> {
                        _loading.value = false
                        if (result.data == true) {
                            isConnected = true
                            startListening(otherUserId, token)
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

    private fun startListening(otherUserId: String, token: String) {
        listeningJob?.cancel()
        listeningJob = viewModelScope.launch {
            chatRepository.incomingMessages(otherUserId, token).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        val newMessages = result.data ?: emptyList()
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

    fun sendMessage(text: String, otherUserId: String) {
        if (!isConnected || text.isBlank()) return

        val token = settings.getPreference("access_token") ?: return

        viewModelScope.launch {
            chatRepository.sendMessage(text, otherUserId, token).collect { result ->
                if (result is NetworkResult.Error) {
                    println("Send failed: ${result.message}")
                }
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            chatRepository.close()
        }
        super.onCleared()
    }
}