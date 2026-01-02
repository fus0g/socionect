package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.CoalitionMessage
import kotlinx.serialization.Serializable

@Serializable
data class CoalitionMessageDTO(
    val id: String = "",
    val coalitionId: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0
)

fun CoalitionMessageDTO.toCoalitionMessage(): CoalitionMessage = CoalitionMessage(
    id = id,
    coalitionId = coalitionId,
    senderId = senderId,
    message = message,
    timestamp = timestamp
)