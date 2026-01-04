package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface OrganisationRepository {

    suspend fun createOrganisation(organisation: Organisation,token: String): Flow<NetworkResult<Organisation>>

    suspend fun getAllOrganisations(token: String): Flow<NetworkResult<List<Organisation>>>

    suspend fun getUserOrganisations(token: String): Flow<NetworkResult<List<Organisation>>>

    suspend fun getOrganisationById(id: String,token: String): Flow<NetworkResult<Organisation>>

    suspend fun getOrganisationMembers(id: String,token: String): Flow<NetworkResult<List<OrganisationMember>>>

    suspend fun sendRequestToUser(organisationId: String, userId: String,token: String): Flow<NetworkResult<Boolean>>

    suspend fun getCurrentUsersOrganisation(token: String): Flow<NetworkResult<Organisation>>
}