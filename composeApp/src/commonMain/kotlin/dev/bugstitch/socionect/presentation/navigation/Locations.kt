package dev.bugstitch.socionect.presentation.navigation

import dev.bugstitch.socionect.data.models.OrganisationDTO
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
object CreateOrganisation

@Serializable
object DiscoverOrganisations

@Serializable
data class OrganisationMainScreen(
    val orgId: String,
    val orgName: String,
    val orgDescription: String,
    val orgCreatedAt: Long
)

@Serializable
data class CreateOrganisationSubtopic(
    val orgId: String,
    val orgName: String,
    val orgDescription: String,
    val orgCreatedAt: Long
)

@Serializable
data class OrganisationReceivedRequests(
    val orgId: String
)

@Serializable
data class ChatRoom(
    val otherUserId: String,
    val otherUserName: String
)

