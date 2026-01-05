package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.RequestSendByOrganisation
import kotlinx.serialization.Serializable

@Serializable
data class RequestSendByOrganisationDTO(
    val id: String = "",
    val organisationId: String = "",
    val userId: String = "",
    val sentAt: Long = 0
)

fun RequestSendByOrganisationDTO.toRequestSendByOrganisation(): RequestSendByOrganisation = RequestSendByOrganisation(
    id = id,
    organisationId = organisationId,
    userId = userId,
    sentAt = sentAt
)
