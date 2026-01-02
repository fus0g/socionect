package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.CoalitionOrganisationMember
import kotlinx.serialization.Serializable

@Serializable
data class CoalitionOrganisationMemberDTO(
    val id: String = "",
    val coalitionId: String = "",
    val userId: String = "",
    val organisationId: String = "",
    val joinedAt: Long = 0,
    val isAdmin: Boolean = false,
    val canSendMessage: Boolean = false
)

fun CoalitionOrganisationMemberDTO.toCoalitionOrganisationMember(): CoalitionOrganisationMember = CoalitionOrganisationMember(
    id = id,
    coalitionId = coalitionId,
    userId = userId,
    organisationId = organisationId,
    joinedAt = joinedAt,
    isAdmin = isAdmin,
    canSendMessage = canSendMessage
)