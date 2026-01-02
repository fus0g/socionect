package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.OrganisationJoinReceivedRequest
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationJoinReceivedRequestDTO(
    val id: String = "",
    val organisationId: String = "",
    val userId: String = "",
    val receivedAt: Long = 0
)

fun OrganisationJoinReceivedRequestDTO.toOrganisationJoinReceivedRequest(): OrganisationJoinReceivedRequest = OrganisationJoinReceivedRequest(
    id = id,
    organisationId = organisationId,
    userId = userId,
    receivedAt = receivedAt
)