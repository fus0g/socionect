package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.CoalitionOrganisation
import kotlinx.serialization.Serializable

@Serializable
data class CoalitionOrganisationDTO(
    val id: String = "",
    val coalitionId: String = "",
    val organisationId: String = "",
    val joinedAt: Long = 0
)

fun CoalitionOrganisationDTO.toCoalitionOrganisation(): CoalitionOrganisation = CoalitionOrganisation(
    id = id,
    coalitionId = coalitionId,
    organisationId = organisationId,
    joinedAt = joinedAt
)