package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.models.RequestSendByUser
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface OrganisationRepository {

    suspend fun createOrganisation(organisation: Organisation,token: String): Flow<NetworkResult<Organisation>>

    suspend fun getUserOrganisations(token: String): Flow<NetworkResult<List<Organisation>>>

    suspend fun searchOrganisation(query: String,token: String): Flow<NetworkResult<List<Organisation>>>

    suspend fun userSendsRequestToOrganisation(organisationId: String,token: String): Flow<NetworkResult<Boolean>>

    suspend fun getAllRequestsSendByUserToOrg(token: String): Flow<NetworkResult<List<Organisation>>>

    suspend fun getRequestsReceivedByOrganisation(organisationId: String,token: String): Flow<NetworkResult<List<User>>>

    suspend fun organisationAcceptsRequest(userId: String,organisationId: String,token: String): Flow<NetworkResult<Boolean>>

    suspend fun organisationDeclinesRequest(userId: String,organisationId: String,token: String): Flow<NetworkResult<Boolean>>

}
