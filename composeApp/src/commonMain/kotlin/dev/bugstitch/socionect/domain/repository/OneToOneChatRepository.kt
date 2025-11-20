package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.ChatMessage
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface OneToOneChatRepository {

    suspend fun connect(otherUserId: String, token: String): Flow<NetworkResult<Boolean>>

    suspend fun incomingMessages(otherUserId: String,token: String): Flow<NetworkResult<List<ChatMessage>>>

    suspend fun sendMessage(text: String,otherUserId: String,token: String): Flow<NetworkResult<Boolean>>

    suspend fun close()
}
