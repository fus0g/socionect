package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.CoalitionMessage
import kotlinx.serialization.Serializable

@Serializable
data class CoalitionMessageDTO(
    val id: String = "",
    val coalitionId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = 0
)

fun CoalitionMessageDTO.toCoalitionMessage(): CoalitionMessage = CoalitionMessage(
    id = id,
    coalitionId = coalitionId,
    senderId = senderId,
    senderName = senderName,
    message = message,
    timestamp = timestamp
)