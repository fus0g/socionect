package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.EMU_SERVER_WS
import dev.bugstitch.socionect.SERVER_WS
import dev.bugstitch.socionect.WEB_SERVER_WS
import dev.bugstitch.socionect.data.models.ChatMessageDTO
import dev.bugstitch.socionect.data.models.toChatMessage
import dev.bugstitch.socionect.domain.models.ChatMessage
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.utils.CustomLog
import dev.bugstitch.socionect.utils.NetworkResult
import dev.bugstitch.socionect.utils.platform
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.websocket.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class OneToOneChatRepositoryImpl(
    private val httpClient: HttpClient
) : OneToOneChatRepository {

    private val endpoint =
        if (platform == "android") EMU_SERVER_WS
        else if (platform == "web") WEB_SERVER_WS
        else SERVER_WS

    private var socket: DefaultClientWebSocketSession? = null

    override suspend fun connect(otherUserId: String, token: String): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val url = "$endpoint/oneToOne/$otherUserId"

                socket = httpClient.webSocketSession {
                    url(url)
                    header("Authorization", "Bearer $token")
                }

                if (socket?.isActive == true) {
                    emit(NetworkResult.Success(true))
                } else {
                    emit(NetworkResult.Error("Connection failed"))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                emit(NetworkResult.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    override suspend fun incomingMessages(otherUserId: String, token: String): Flow<NetworkResult<List<ChatMessage>>> {
        return flow {
            try {
                val session = socket ?: throw Exception("Socket not initialized")

                while (session.isActive) {
                    val dto = session.receiveDeserialized<ChatMessageDTO>()
                    val domainMessage = dto.toChatMessage()
                    emit(NetworkResult.Success(listOf(domainMessage)))
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                emit(NetworkResult.Error(e.message ?: "Connection lost"))
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun sendMessage(text: String, otherUserId: String, token: String): Flow<NetworkResult<Boolean>> {
        return flow {
            try {
                val session = socket ?: return@flow emit(NetworkResult.Error("No connection"))

                val requestDto = ChatMessageDTO(
                    messageId = 0,
                    chatId = "",
                    senderId = "",
                    message = text,
                    timestamp = Clock.System.now().toEpochMilliseconds()
                )
                session.sendSerialized(requestDto)
                emit(NetworkResult.Success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(NetworkResult.Error(e.message ?: "Failed to send"))
            }
        }
    }

    override suspend fun close() {
        socket?.close()
        socket = null
    }
}