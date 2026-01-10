package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.ChatMessage
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDTO(
    val messageId: Long,
    val chatId: String,
    val senderId: String,
    val message: String,
    val timestamp: Long
)

fun ChatMessageDTO.toChatMessage(): ChatMessage =
    ChatMessage(messageId, chatId, senderId, message, timestamp)