package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.OrganisationJoinSentRequestDTO

data class OrganisationJoinSentRequest(
    val id: String,
    val organisationId: String,
    val userId: String,
    val sentAt: Long
)

fun OrganisationJoinSentRequest.toOrganisationJoinSentRequestDTO(): OrganisationJoinSentRequestDTO = OrganisationJoinSentRequestDTO(
    id = id,
    organisationId = organisationId,
    userId = userId,
    sentAt = sentAt
)