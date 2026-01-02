package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.OrganisationMember
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationMemberDTO(
    val id: String = "",
    val organisationId: String = "",
    val userId: String = "",
    val role: Int = 0,
    val joinedAt: Long = 0
)

fun OrganisationMemberDTO.toOrganisationMember(): OrganisationMember = OrganisationMember(
    id = id,
    organisationId = organisationId,
    userId = userId,
    role = role,
    joinedAt = joinedAt
)