package dev.bugstitch.socionect.routes

import dev.bugstitch.socionect.data.models.CoalitionMessageDTO
import dev.bugstitch.socionect.data.models.OrganisationSubtopicMessageDTO
import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import dev.bugstitch.socionect.domain.models.toCoalitionMessageDTO
import dev.bugstitch.socionect.domain.models.toOrganisationSubtopicMessageDTO
import dev.bugstitch.socionect.domain.repository.OrganisationChatRepository
import dev.bugstitch.socionect.utils.ConnectionManager
import dev.bugstitch.socionect.utils.routes.ChatRoutes
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.routing
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException

fun Application.organisationChatRouting(
    organisationChatRepository: OrganisationChatRepository
) {

    routing {
        authenticate("auth-jwt-user") {

            webSocket("${ChatRoutes.SUBTOPIC_CHAT}{${ChatRoutes.KEY}}") {
                val principal = call.principal<JWTPrincipal>()
                val senderId = principal?.getClaim("id", String::class)
                    ?: return@webSocket close(
                        CloseReason(
                            CloseReason.Codes.VIOLATED_POLICY,
                            "Missing sender id"
                        )
                    )

                val subtopicId = call.parameters[ChatRoutes.KEY]
                    ?: return@webSocket close(
                        CloseReason(
                            CloseReason.Codes.CANNOT_ACCEPT,
                            "Missing subtopic id"
                        )
                    )


                ConnectionManager.addConnection(subtopicId, this)

                val previous = organisationChatRepository.getAllSubTopicChats(senderId,subtopicId)
                previous.forEach { msg ->
                    sendSerialized(msg.toOrganisationSubtopicMessageDTO())
                }

                try {
                    while (true) {
                        val incomingDto = receiveDeserialized<OrganisationSubtopicMessageDTO>()
                        val messageText = incomingDto.message

                        val success = organisationChatRepository.createSubTopicMessage(
                            userId = senderId,
                            organisationSubtopicMessage = OrganisationSubtopicMessage(
                                organisationSubtopicId = subtopicId,
                                senderId = senderId,
                                message = messageText
                            ),
                        )

                        if (!success) {
                            sendSerialized(
                                OrganisationSubtopicMessageDTO(
                                    message = "Failed to send message",
                                )
                            )
                            continue
                        }

                        val savedMessages = organisationChatRepository.getAllSubTopicChats(senderId,subtopicId)
                        val lastMessage = savedMessages.lastOrNull() ?: continue
                        val dto = lastMessage.toOrganisationSubtopicMessageDTO()

                        ConnectionManager.getConnections(subtopicId).forEach { session ->
                            session.sendSerialized(dto)
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    ConnectionManager.removeConnection(subtopicId, this)
                    close()
                }
            }


            webSocket("${ChatRoutes.COALITION_CHAT}{${ChatRoutes.KEY}}") {
                val principal = call.principal<JWTPrincipal>()
                val senderId = principal?.getClaim("id", String::class)
                    ?: return@webSocket close(
                        CloseReason(
                            CloseReason.Codes.VIOLATED_POLICY,
                            "Missing sender id"
                        )
                    )

                val coalition = call.parameters[ChatRoutes.KEY]
                    ?: return@webSocket close(
                        CloseReason(
                            CloseReason.Codes.CANNOT_ACCEPT,
                            "Missing coalition id"
                        )
                    )


                ConnectionManager.addConnection(coalition, this)

                val previous = organisationChatRepository.getAllCoalitionChats(senderId,coalition)
                previous.forEach { msg ->
                    sendSerialized(msg.toCoalitionMessageDTO())
                }

                try {
                    while (true) {
                        val incomingDto = receiveDeserialized<CoalitionMessageDTO>()
                        val messageText = incomingDto.message

                        val success = organisationChatRepository.createCoalitionMessage(
                            userId = senderId,
                            message = CoalitionMessage(
                                coalitionId = coalition,
                                senderId = senderId,
                                message = messageText
                            ),
                        )

                        if (!success) {
                            sendSerialized(
                                CoalitionMessageDTO(
                                    message = "Failed to send message",
                                )
                            )
                            continue
                        }

                        val savedMessages = organisationChatRepository.getAllCoalitionChats(senderId,coalition)
                        val lastMessage = savedMessages.lastOrNull() ?: continue
                        val dto = lastMessage.toCoalitionMessageDTO()

                        ConnectionManager.getConnections(coalition).forEach { session ->
                            session.sendSerialized(dto)
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    ConnectionManager.removeConnection(coalition, this)
                    close()
                }
            }

        }
    }

}