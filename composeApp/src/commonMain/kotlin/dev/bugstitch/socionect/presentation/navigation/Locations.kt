package dev.bugstitch.socionect.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Signup

@Serializable
object Login

@Serializable
object Landing

@Serializable
object Discover

@Serializable
data class ChatRoom(
    val otherUserId: String,
    val otherUserName: String
)

