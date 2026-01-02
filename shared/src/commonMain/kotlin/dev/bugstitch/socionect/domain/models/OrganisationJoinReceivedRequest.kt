package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.OrganisationJoinReceivedRequestDTO

data class OrganisationJoinReceivedRequest(
    val id: String,
    val organisationId: String,
    val userId: String,
    val receivedAt: Long
)

fun OrganisationJoinReceivedRequest.toOrganisationJoinReceivedRequestDTO(): OrganisationJoinReceivedRequestDTO = OrganisationJoinReceivedRequestDTO(
    id = id,
    organisationId = organisationId,
    userId = userId,
    receivedAt = receivedAt

)