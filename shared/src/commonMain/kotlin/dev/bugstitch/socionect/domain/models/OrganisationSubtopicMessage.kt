package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.OrganisationSubtopicMessageDTO

data class OrganisationSubtopicMessage(
    val id: String = "",
    val organisationSubtopicId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = 0
)

fun OrganisationSubtopicMessage.toOrganisationSubtopicMessageDTO(): OrganisationSubtopicMessageDTO = OrganisationSubtopicMessageDTO(
    id = id,
    organisationSubtopicId = organisationSubtopicId,
    senderId = senderId,
    message = message,
    timestamp = timestamp,
    senderName = senderName
)
