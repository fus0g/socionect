package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.CoalitionOrganisationMemberDTO

data class CoalitionOrganisationMember(
    val id: String,
    val coalitionId: String,
    val userId: String,
    val organisationId: String,
    val joinedAt: Long,
    val isAdmin: Boolean,
    val canSendMessage: Boolean
)

fun CoalitionOrganisationMember.toCoalitionOrganisationMemberDTO(): CoalitionOrganisationMemberDTO = CoalitionOrganisationMemberDTO(
    id = id,
    coalitionId = coalitionId,
    userId = userId,
    organisationId = organisationId,
    joinedAt = joinedAt,
    isAdmin = isAdmin,
    canSendMessage = canSendMessage

)