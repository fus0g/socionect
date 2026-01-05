package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.RequestSendByUserDTO

data class RequestSendByUser(
    val id: String,
    val organisationId: String,
    val userId: String,
    val receivedAt: Long
)

fun RequestSendByUser.toRequestSendByUserDTO(): RequestSendByUserDTO = RequestSendByUserDTO(
    id = id,
    organisationId = organisationId,
    userId = userId,
    receivedAt = receivedAt

)