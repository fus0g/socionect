package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.Organisation
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationDTO(
    val id:String = "",
    val name: String = "",
    val description: String = "",
    val createdAt: Long = 0
)

fun OrganisationDTO.toOrganisation(): Organisation = Organisation(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt
)