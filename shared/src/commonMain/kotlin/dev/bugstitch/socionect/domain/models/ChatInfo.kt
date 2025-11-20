package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.ChatInfoDTO

data class ChatInfo(
    val chatId: String,
    val userId1: String,
    val userId2: String,
    val createdAt: Long,
)

fun ChatInfo.toChatInfoDTO(): ChatInfoDTO = ChatInfoDTO(
    chatId = chatId,
    userId1 = userId1,
    userId2 = userId2,
    createdAt = createdAt
)