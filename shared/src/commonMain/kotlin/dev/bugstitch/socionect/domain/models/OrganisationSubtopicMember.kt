package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.OrganisationSubtopicMemberDTO

data class OrganisationSubtopicMember(
    val id: String,
    val organisationSubtopicId: String,
    val userId: String,
    val joinedAt: Long,
    val isAdmin: Boolean,
    val canSendMessage: Boolean
)

fun OrganisationSubtopicMember.toOrganisationSubtopicMemberDTO(): OrganisationSubtopicMemberDTO = OrganisationSubtopicMemberDTO(
    id = id,
    organisationSubtopicId = organisationSubtopicId,
    userId = userId,
    joinedAt = joinedAt,
    isAdmin = isAdmin,
    canSendMessage = canSendMessage
)
