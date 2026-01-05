package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.RequestSendByUser
import kotlinx.serialization.Serializable

@Serializable
data class RequestSendByUserDTO(
    val id: String = "",
    val organisationId: String = "",
    val userId: String = "",
    val receivedAt: Long = 0
)

fun RequestSendByUserDTO.toRequestSendByUser(): RequestSendByUser = RequestSendByUser(
    id = id,
    organisationId = organisationId,
    userId = userId,
    receivedAt = receivedAt
)