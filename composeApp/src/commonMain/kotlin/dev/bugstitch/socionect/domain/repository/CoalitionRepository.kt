package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface CoalitionRepository {

    suspend fun createCoalition(
        token: String,
        hostOrg: Organisation,
        organisations: List<Organisation>,
        name: String,
        description: String
    ): Flow<NetworkResult<Boolean>>

    suspend fun getAllCoalitions(token: String,organisation: Organisation): Flow<NetworkResult<List<Coalition>>>

    suspend fun getAllOrganisations(token: String,coalition: Coalition): Flow<NetworkResult<List<Organisation>>>

    suspend fun getAllRequests(token: String,organisation: Organisation): Flow<NetworkResult<List<CoalitionRequest>>>

    suspend fun createRequest(token: String,coalition: Coalition,organisation: Organisation): Flow<NetworkResult<Boolean>>

    suspend fun acceptRequest(token: String,coalitionRequest: CoalitionRequest): Flow<NetworkResult<Boolean>>

    suspend fun declineRequest(token: String,coalitionRequest: CoalitionRequest): Flow<NetworkResult<Boolean>>

}