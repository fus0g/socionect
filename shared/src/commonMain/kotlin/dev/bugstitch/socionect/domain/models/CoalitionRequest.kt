package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.CoalitionRequestDTO

data class CoalitionRequest(
    val id: String = "",
    val coalitionId: String = "",
    val organisationId: String = "",
    val sentAt: Long = 0,
    val coalition: Coalition = Coalition()
)

fun CoalitionRequest.coalitionRequestDTO(): CoalitionRequestDTO = CoalitionRequestDTO(
    id = id,
    coalitionId = coalitionId,
    organisationId = organisationId,
    sentAt = sentAt,
    coalition = coalition.toCoalitionDTO()
)