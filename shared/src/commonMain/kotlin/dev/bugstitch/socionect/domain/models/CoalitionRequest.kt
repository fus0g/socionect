package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.CoalitionRequestDTO

data class CoalitionRequest(
    val id: String,
    val coalitionId: String,
    val organisationId: String,
    val sentAt: Long
)

fun CoalitionRequest.coalitionRequestDTO(): CoalitionRequestDTO = CoalitionRequestDTO(
    id = id,
    coalitionId = coalitionId,
    organisationId = organisationId,
    sentAt = sentAt
)