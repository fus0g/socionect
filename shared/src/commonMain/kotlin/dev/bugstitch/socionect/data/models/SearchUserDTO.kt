package dev.bugstitch.socionect.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSearchRequest(val query: String)
