package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.RequestSendByOrganisationDTO

data class RequestSendByOrganisation(
    val id: String,
    val organisationId: String,
    val userId: String,
    val sentAt: Long
)

fun RequestSendByOrganisation.toRequestSendByOrganisationDTO(): RequestSendByOrganisationDTO = RequestSendByOrganisationDTO(
    id = id,
    organisationId = organisationId,
    userId = userId,
    sentAt = sentAt
)