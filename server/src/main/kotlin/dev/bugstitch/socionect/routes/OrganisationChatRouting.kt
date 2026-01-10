package dev.bugstitch.socionect.routes

import dev.bugstitch.socionect.data.models.CoalitionMessageDTO
import dev.bugstitch.socionect.data.models.OrganisationSubtopicMessageDTO
import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import dev.bugstitch.socionect.domain.models.toCoalitionMessageDTO
import dev.bugstitch.socionect.domain.models.toOrganisationSubtopicMessageDTO
import dev.bugstitch.socionect.domain.repository.OrganisationChatRepository
import dev.bugstitch.socionect.modules.JwtConfig
import dev.bugstitch.socionect.utils.ConnectionManager
import dev.bugstitch.socionect.utils.routes.ChatRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException

fun Application.organisationChatRouting(
    organisationChatRepository: OrganisationChatRepository
) {
    routing {

        webSocket("${ChatRoutes.SUBTOPIC_CHAT}{${ChatRoutes.KEY}}") {

            val senderId = authenticateWebSocket() ?: return@webSocket

            val subtopicId = call.parameters[ChatRoutes.KEY]
                ?: return@webSocket close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "Missing subtopic id"
                    )
                )

            ConnectionManager.addConnection(subtopicId, this)

            // Send previous messages
            organisationChatRepository
                .getAllSubTopicChats(senderId, subtopicId)
                .forEach {
                    sendSerialized(it.toOrganisationSubtopicMessageDTO())
                }

            try {
                while (true) {
                    val incoming = receiveDeserialized<OrganisationSubtopicMessageDTO>()

                    val success =
                        organisationChatRepository.createSubTopicMessage(
                            userId = senderId,
                            organisationSubtopicMessage =
                                OrganisationSubtopicMessage(
                                    organisationSubtopicId = subtopicId,
                                    senderId = senderId,
                                    message = incoming.message
                                )
                        )

                    if (!success) continue

                    val last =
                        organisationChatRepository
                            .getAllSubTopicChats(senderId, subtopicId)
                            .lastOrNull()
                            ?: continue

                    val dto = last.toOrganisationSubtopicMessageDTO()

                    ConnectionManager
                        .getConnections(subtopicId)
                        .forEach { it.sendSerialized(dto) }
                }
            } catch (_: ClosedReceiveChannelException) {
            } finally {
                ConnectionManager.removeConnection(subtopicId, this)
                close()
            }
        }

        webSocket("${ChatRoutes.COALITION_CHAT}{${ChatRoutes.KEY}}") {

            val senderId = authenticateWebSocket() ?: return@webSocket

            val coalitionId = call.parameters[ChatRoutes.KEY]
                ?: return@webSocket close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "Missing coalition id"
                    )
                )

            ConnectionManager.addConnection(coalitionId, this)

            organisationChatRepository
                .getAllCoalitionChats(senderId, coalitionId)
                .forEach {
                    sendSerialized(it.toCoalitionMessageDTO())
                }

            try {
                while (true) {
                    val incoming = receiveDeserialized<CoalitionMessageDTO>()

                    val success =
                        organisationChatRepository.createCoalitionMessage(
                            userId = senderId,
                            message =
                                CoalitionMessage(
                                    coalitionId = coalitionId,
                                    senderId = senderId,
                                    message = incoming.message
                                )
                        )

                    if (!success) continue

                    val last =
                        organisationChatRepository
                            .getAllCoalitionChats(senderId, coalitionId)
                            .lastOrNull()
                            ?: continue

                    val dto = last.toCoalitionMessageDTO()

                    ConnectionManager
                        .getConnections(coalitionId)
                        .forEach { it.sendSerialized(dto) }
                }
            } catch (_: ClosedReceiveChannelException) {
            } finally {
                ConnectionManager.removeConnection(coalitionId, this)
                close()
            }
        }
    }
}

private suspend fun DefaultWebSocketServerSession.authenticateWebSocket(): String? {

    val token =
        call.request.queryParameters["token"]
            ?: call.request.headers["Authorization"]
                ?.removePrefix("Bearer ")

    if (token == null) {
        close(
            CloseReason(
                CloseReason.Codes.VIOLATED_POLICY,
                "Missing token"
            )
        )
        return null
    }

    val decoded = try {
        JwtConfig.verifier.verify(token)
    } catch (e: Exception) {
        null
    }

    if (decoded == null) {
        close(
            CloseReason(
                CloseReason.Codes.VIOLATED_POLICY,
                "Invalid token"
            )
        )
        return null
    }

    return decoded.getClaim("id").asString()
}
