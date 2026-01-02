package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.OrganisationMemberDTO

data class OrganisationMember(
    val id: String,
    val organisationId: String,
    val userId: String,
    val role: Int,
    val joinedAt: Long
)

fun OrganisationMember.toOrganisationMemberDTO(): OrganisationMemberDTO = OrganisationMemberDTO(
    id = id,
    organisationId = organisationId,
    userId = userId,
    role = role,
    joinedAt = joinedAt
)