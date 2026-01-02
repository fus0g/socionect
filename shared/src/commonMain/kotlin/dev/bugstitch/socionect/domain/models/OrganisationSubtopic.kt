package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.OrganisationSubtopicDTO

data class OrganisationSubtopic(
    val id: String,
    val name: String,
    val description: String,
    val organisationId: String,
    val createdAt: Long
)

fun OrganisationSubtopic.toOrganisationSubtopicDTO(): OrganisationSubtopicDTO = OrganisationSubtopicDTO(
    id = id,
    name = name,
    description = description,
    organisationId = organisationId,
    createdAt = createdAt
)