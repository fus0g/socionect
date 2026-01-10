package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.EMU_SERVER_WS
import dev.bugstitch.socionect.SERVER_WS
import dev.bugstitch.socionect.WEB_SERVER_WS
import dev.bugstitch.socionect.data.models.CoalitionMessageDTO
import dev.bugstitch.socionect.data.models.OrganisationSubtopicMessageDTO
import dev.bugstitch.socionect.data.models.toCoalitionMessage
import dev.bugstitch.socionect.data.models.toOrganisationSubtopicMessage
import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import dev.bugstitch.socionect.domain.repository.OrganisationChatRepository
import dev.bugstitch.socionect.utils.NetworkResult
import dev.bugstitch.socionect.utils.platform
import dev.bugstitch.socionect.utils.routes.ChatRoutes
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.websocket.close
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class OrganisationChatRepositoryImpl(
    private val httpClient: HttpClient
) : OrganisationChatRepository {

    private val endpoint = when (platform) {
        "android" -> EMU_SERVER_WS
        "web" -> WEB_SERVER_WS
        else -> SERVER_WS
    }

    private var subtopicSocket: DefaultClientWebSocketSession? = null
    private var coalitionSocket: DefaultClientWebSocketSession? = null


    override suspend fun connectSubtopicChat(
        subtopicId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> = flow {

        emit(NetworkResult.Loading())

        try {
            subtopicSocket?.close()

            val url = buildUrl(ChatRoutes.SUBTOPIC_CHAT, subtopicId, token)

            subtopicSocket = httpClient.webSocketSession {
                url(url)
                if (platform != "web") {
                    header("Authorization", "Bearer $token")
                }
            }

            emit(
                if (subtopicSocket?.isActive == true)
                    NetworkResult.Success(true)
                else
                    NetworkResult.Error("Subtopic connection failed")
            )

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Subtopic connection error"))
        }
    }

    override suspend fun connectCoalitionChat(
        coalitionId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> = flow {

        emit(NetworkResult.Loading())

        try {
            coalitionSocket?.close()

            val url = buildUrl(ChatRoutes.COALITION_CHAT, coalitionId, token)

            coalitionSocket = httpClient.webSocketSession {
                url(url)
                if (platform != "web") {
                    header("Authorization", "Bearer $token")
                }
            }

            emit(
                if (coalitionSocket?.isActive == true)
                    NetworkResult.Success(true)
                else
                    NetworkResult.Error("Coalition connection failed")
            )

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Coalition connection error"))
        }
    }


    override suspend fun incomingSubtopicMessages(
        subtopicId: String,
        token: String
    ): Flow<NetworkResult<List<OrganisationSubtopicMessage>>> = flow {

        val session = subtopicSocket
            ?: return@flow emit(NetworkResult.Error("Subtopic socket not connected"))

        try {
            while (session.isActive) {
                val dto = session.receiveDeserialized<OrganisationSubtopicMessageDTO>()
                emit(NetworkResult.Success(listOf(dto.toOrganisationSubtopicMessage())))
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(NetworkResult.Error(e.message ?: "Subtopic connection lost"))
        }
    }

    override suspend fun incomingCoalitionMessages(
        coalitionId: String,
        token: String
    ): Flow<NetworkResult<List<CoalitionMessage>>> = flow {

        val session = coalitionSocket
            ?: return@flow emit(NetworkResult.Error("Coalition socket not connected"))

        try {
            while (session.isActive) {
                val dto = session.receiveDeserialized<CoalitionMessageDTO>()
                emit(NetworkResult.Success(listOf(dto.toCoalitionMessage())))
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(NetworkResult.Error(e.message ?: "Coalition connection lost"))
        }
    }


    override suspend fun sendSubtopicMessage(
        text: String,
        subtopicId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> = flow {

        val session = subtopicSocket
            ?: return@flow emit(NetworkResult.Error("Subtopic socket not connected"))

        try {
            session.sendSerialized(
                OrganisationSubtopicMessageDTO(
                    organisationSubtopicId = subtopicId,
                    message = text
                )
            )
            emit(NetworkResult.Success(true))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Failed to send subtopic message"))
        }
    }

    override suspend fun sendCoalitionMessage(
        text: String,
        coalitionId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> = flow {

        val session = coalitionSocket
            ?: return@flow emit(NetworkResult.Error("Coalition socket not connected"))

        try {
            session.sendSerialized(
                CoalitionMessageDTO(
                    coalitionId = coalitionId,
                    message = text
                )
            )
            emit(NetworkResult.Success(true))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Failed to send coalition message"))
        }
    }


    override suspend fun detach() {
        subtopicSocket?.close()
        coalitionSocket?.close()
        subtopicSocket = null
        coalitionSocket = null
    }


    private fun buildUrl(
        base: String,
        id: String,
        token: String
    ): String =
        if (platform == "web")
            "$endpoint$base$id?token=$token"
        else
            "$endpoint$base$id"
}
