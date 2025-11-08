package dev.bugstitch.socionect.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TokenDTO(
    val token: String,
    val refreshToken: String
)
