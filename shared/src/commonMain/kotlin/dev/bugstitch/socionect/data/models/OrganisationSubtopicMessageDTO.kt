package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import kotlinx.serialization.Serializable

@Serializable
data class OrganisationSubtopicMessageDTO(
    val id: String = "",
    val organisationSubtopicId: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0
)

fun OrganisationSubtopicMessageDTO.toOrganisationSubtopicMessage(): OrganisationSubtopicMessage = OrganisationSubtopicMessage(
    id = id,
    organisationSubtopicId = organisationSubtopicId,
    senderId = senderId,
    message = message,
    timestamp = timestamp
)