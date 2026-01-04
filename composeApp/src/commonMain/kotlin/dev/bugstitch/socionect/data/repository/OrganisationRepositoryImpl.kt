package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.EMU_SERVER
import dev.bugstitch.socionect.SERVER
import dev.bugstitch.socionect.WEB_SERVER
import dev.bugstitch.socionect.data.models.OrganisationDTO
import dev.bugstitch.socionect.data.models.toOrganisation
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.NetworkResult
import dev.bugstitch.socionect.utils.platform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrganisationRepositoryImpl(
    private val httpClient: HttpClient
) : OrganisationRepository {

    private val endpoint =
        when (platform) {
            "android" -> EMU_SERVER
            "web" -> WEB_SERVER
            else -> SERVER
        }

    override suspend fun createOrganisation(
        organisation: Organisation,
        token: String
    ): Flow<NetworkResult<Organisation>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint/organisation/create"){
                    contentType(ContentType.Application.Json)
                    setBody(OrganisationDTO(
                        name = organisation.name,
                        description = organisation.description
                    ))
                    header("Authorization", "Bearer $token")
                }
                if(response.status == HttpStatusCode.OK)
                {
                    val org = response.body<OrganisationDTO>()
                    emit(NetworkResult.Success(org.toOrganisation()))
                }
                else
                {
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getUserOrganisations(token: String): Flow<NetworkResult<List<Organisation>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.get("$endpoint/organisation/userOrganisations"){
                    header("Authorization", "Bearer $token")
                }
                if(response.status == HttpStatusCode.OK)
                {
                    val orgs = response.body<List<OrganisationDTO>>()
                    emit(NetworkResult.Success(orgs.map { it.toOrganisation() }))
                }
                else
                {
                    emit(NetworkResult.Error(response.status.description))
                }

            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }

    }

    override suspend fun getAllOrganisations(token: String): Flow<NetworkResult<List<Organisation>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrganisationById(
        id: String,
        token: String
    ): Flow<NetworkResult<Organisation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrganisationMembers(
        id: String,
        token: String
    ): Flow<NetworkResult<List<OrganisationMember>>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendRequestToUser(
        organisationId: String,
        userId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUsersOrganisation(
        token: String
    ): Flow<NetworkResult<Organisation>> {
        TODO("Not yet implemented")
    }
}