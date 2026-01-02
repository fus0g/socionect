package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationSubtopicDTO(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val organisationId: String = "",
    val createdAt: Long = 0
)

fun OrganisationSubtopicDTO.toOrganisationSubtopic(): OrganisationSubtopic = OrganisationSubtopic(
    id = id,
    name = name,
    description = description,
    organisationId = organisationId,
    createdAt = createdAt
)