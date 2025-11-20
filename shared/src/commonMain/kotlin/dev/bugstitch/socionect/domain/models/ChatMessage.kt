package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.ChatMessageDTO

data class ChatMessage(
    val messageId: Long,
    val chatId: String,
    val senderId: String,
    val message: String,
    val timestamp: Long
)

fun ChatMessage.toChatMessageDTO(): ChatMessageDTO = ChatMessageDTO(
    messageId = this.messageId,
    chatId = this.chatId,
    senderId = this.senderId,
    message = this.message,
    timestamp = this.timestamp
)
