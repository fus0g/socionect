package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.CoalitionRequest
import kotlinx.serialization.Serializable

@Serializable
data class CoalitionRequestDTO(
    val id: String = "",
    val coalitionId: String = "",
    val organisationId: String = "",
    val sentAt: Long = 0
)

fun CoalitionRequestDTO.toCoalitionRequest(): CoalitionRequest = CoalitionRequest(
    id = id,
    coalitionId = coalitionId,
    organisationId = organisationId,
    sentAt = sentAt
)
