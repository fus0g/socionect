package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.ChatInfo
import kotlinx.serialization.Serializable

@Serializable
data class ChatInfoDTO(
    val chatId: String,
    val userId1: String,
    val userId2: String,
    val createdAt: Long
)

fun ChatInfoDTO.toChatInfo(): ChatInfo{
    return  ChatInfo(chatId, userId1, userId2, createdAt)
}
