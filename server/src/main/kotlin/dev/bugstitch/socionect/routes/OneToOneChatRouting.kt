package dev.bugstitch.socionect.routes

import dev.bugstitch.socionect.data.models.ChatMessageDTO
import dev.bugstitch.socionect.domain.models.toChatMessageDTO
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.utils.ConnectionManager
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException

fun Application.oneToOneChatRouting(chatRepository: OneToOneChatRepository) {

    routing {
        authenticate("auth-jwt-user") {

            webSocket("/oneToOne/{receiverId}") {
                val principal = call.principal<JWTPrincipal>()
                val senderId = principal?.getClaim("id", String::class)
                    ?: return@webSocket close(
                        CloseReason(
                            CloseReason.Codes.VIOLATED_POLICY,
                            "Missing sender id"
                        )
                    )

                val receiverId = call.parameters["receiverId"]
                    ?: return@webSocket close(
                        CloseReason(
                            CloseReason.Codes.CANNOT_ACCEPT,
                            "Missing receiver id"
                        )
                    )

                val chatId = listOf(senderId, receiverId).sorted().joinToString("")

                ConnectionManager.addConnection(senderId, this)

                val previous = chatRepository.getMessages(chatId)
                previous.forEach { msg ->
                    sendSerialized(msg.toChatMessageDTO())
                }

                try {
                    while (true) {
                        val incomingDto = receiveDeserialized<ChatMessageDTO>()
                        val messageText = incomingDto.message

                        val success = chatRepository.sendMessage(
                            sender = senderId,
                            receiver = receiverId,
                            message = messageText
                        )

                        if (!success) {
                            sendSerialized(
                                ChatMessageDTO(
                                    0,
                                    chatId,
                                    "System",
                                    "Failed to send",
                                    System.currentTimeMillis()
                                )
                            )
                            continue
                        }

                        val savedMessages = chatRepository.getMessages(chatId)
                        val lastMessage = savedMessages.lastOrNull() ?: continue
                        val dto = lastMessage.toChatMessageDTO()

                        ConnectionManager.getConnections(senderId).forEach { session ->
                            session.sendSerialized(dto)
                        }

                        ConnectionManager.getConnections(receiverId).forEach { session ->
                            session.sendSerialized(dto)
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    close()
                }
            }
        }
    }
}