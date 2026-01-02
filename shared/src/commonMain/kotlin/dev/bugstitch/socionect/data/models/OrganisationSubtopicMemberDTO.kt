package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMember
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationSubtopicMemberDTO(
    val id: String = "",
    val organisationSubtopicId: String = "",
    val userId: String = "",
    val joinedAt: Long = 0,
    val isAdmin: Boolean = false,
    val canSendMessage: Boolean = false
)

fun OrganisationSubtopicMemberDTO.toOrganisationSubtopicMember(): OrganisationSubtopicMember = OrganisationSubtopicMember(
    id = id,
    organisationSubtopicId = organisationSubtopicId,
    userId = userId,
    joinedAt = joinedAt,
    isAdmin = isAdmin,
    canSendMessage = canSendMessage
)
