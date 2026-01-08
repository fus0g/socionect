package dev.bugstitch.socionect.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateCoalitionDTO(
    val coalitionDTO: CoalitionDTO,
    val hostOrg: OrganisationDTO,
    val organisations:List<OrganisationDTO>
)
