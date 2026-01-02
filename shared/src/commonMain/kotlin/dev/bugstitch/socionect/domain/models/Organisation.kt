package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.OrganisationDTO

data class Organisation(
    val id:String,
    val name: String,
    val description: String,
    val createdAt: Long
)

fun Organisation.toOrganisationDTO(): OrganisationDTO = OrganisationDTO(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt
)
