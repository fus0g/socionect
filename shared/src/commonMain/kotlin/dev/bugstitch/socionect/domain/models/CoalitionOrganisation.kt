package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.CoalitionOrganisationDTO

data class CoalitionOrganisation(
    val id: String,
    val coalitionId: String,
    val organisationId: String,
    val joinedAt: Long
)

fun CoalitionOrganisation.toCoalitionOrganisationDTO(): CoalitionOrganisationDTO = CoalitionOrganisationDTO(
    id = id,
    coalitionId = coalitionId,
    organisationId = organisationId,
    joinedAt = joinedAt
)