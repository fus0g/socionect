package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.CoalitionMessageDTO

data class CoalitionMessage(
    val id: String,
    val coalitionId: String,
    val senderId: String,
    val message: String,
    val timestamp: Long
)

fun CoalitionMessage.toCoalitionMessageDTO(): CoalitionMessageDTO = CoalitionMessageDTO(
    id = id,
    coalitionId = coalitionId,
    senderId = senderId,
    message = message,
    timestamp = timestamp
)
