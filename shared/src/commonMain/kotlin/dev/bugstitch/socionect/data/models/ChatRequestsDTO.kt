package dev.bugstitch.socionect.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(val otherUserId: String)

@Serializable
data class SendMessageRequest(val message: String)