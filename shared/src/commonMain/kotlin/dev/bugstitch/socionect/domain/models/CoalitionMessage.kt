package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.CoalitionMessageDTO

data class CoalitionMessage(
    val id: String = "",
    val coalitionId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = 0
)

fun CoalitionMessage.toCoalitionMessageDTO(): CoalitionMessageDTO = CoalitionMessageDTO(
    id = id,
    coalitionId = coalitionId,
    senderId = senderId,
    senderName = senderName,
    message = message,
    timestamp = timestamp
)
