package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.OrganisationJoinSentRequest
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationJoinSentRequestDTO(
    val id: String = "",
    val organisationId: String = "",
    val userId: String = "",
    val sentAt: Long = 0
)

fun OrganisationJoinSentRequestDTO.toOrganisationJoinSentRequest(): OrganisationJoinSentRequest = OrganisationJoinSentRequest(
    id = id,
    organisationId = organisationId,
    userId = userId,
    sentAt = sentAt
)
