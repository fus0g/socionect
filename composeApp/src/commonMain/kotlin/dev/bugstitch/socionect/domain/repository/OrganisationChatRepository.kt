package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface OrganisationChatRepository {

    suspend fun connectSubtopicChat(subtopicId: String, token: String): Flow<NetworkResult<Boolean>>
    suspend fun connectCoalitionChat(coalitionId: String, token: String): Flow<NetworkResult<Boolean>>

    suspend fun incomingSubtopicMessages(subtopicId: String, token: String): Flow<NetworkResult<List<OrganisationSubtopicMessage>>>
    suspend fun incomingCoalitionMessages(coalitionId: String, token: String): Flow<NetworkResult<List<CoalitionMessage>>>

    suspend fun sendSubtopicMessage(text: String, subtopicId: String, token: String): Flow<NetworkResult<Boolean>>
    suspend fun sendCoalitionMessage(text: String, coalitionId: String, token: String): Flow<NetworkResult<Boolean>>

    suspend fun detach()

}