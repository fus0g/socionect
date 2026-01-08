package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.EMU_SERVER
import dev.bugstitch.socionect.SERVER
import dev.bugstitch.socionect.WEB_SERVER
import dev.bugstitch.socionect.data.models.CoalitionDTO
import dev.bugstitch.socionect.data.models.CoalitionRequestDTO
import dev.bugstitch.socionect.data.models.CreateCoalitionDTO
import dev.bugstitch.socionect.data.models.OrganisationDTO
import dev.bugstitch.socionect.data.models.toCoalition
import dev.bugstitch.socionect.data.models.toCoalitionRequest
import dev.bugstitch.socionect.data.models.toOrganisation
import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.coalitionRequestDTO
import dev.bugstitch.socionect.domain.models.toCoalitionDTO
import dev.bugstitch.socionect.domain.models.toOrganisationDTO
import dev.bugstitch.socionect.domain.repository.CoalitionRepository
import dev.bugstitch.socionect.utils.CustomLog
import dev.bugstitch.socionect.utils.NetworkResult
import dev.bugstitch.socionect.utils.platform
import dev.bugstitch.socionect.utils.routes.CoalitionRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class CoalitionRepositoryImpl(
    private val httpClient: HttpClient
): CoalitionRepository{

    private val endpoint =
        when (platform) {
            "android" -> EMU_SERVER
            "web" -> WEB_SERVER
            else -> SERVER
        }

    override suspend fun createCoalition(
        token: String,
        hostOrg: Organisation,
        organisations: List<Organisation>,
        name: String,
        description: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            CustomLog("CREATECOL","$hostOrg $organisations $name $description")
            try {
                val request = httpClient.post("$endpoint${CoalitionRoutes.CREATE}"){
                    ->
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        CreateCoalitionDTO(
                            CoalitionDTO(
                                name = name,
                                description = description
                            ),
                            hostOrg = hostOrg.toOrganisationDTO(),
                            organisations = organisations.map { it.toOrganisationDTO() }
                        )
                    )
                }
                if(request.status == HttpStatusCode.Created) {
                    CustomLog("CREATECOL",request.body())
                    emit(NetworkResult.Success(true))
                }else{
                    CustomLog("CREATECOL",request.status.description)
                    emit(NetworkResult.Error(request.status.description))
                }
            }catch (e: Exception){
                CustomLog("CREATECOL",e.message.toString())
                emit(NetworkResult.Error(e.message.toString()))
            }

        }
    }

    override suspend fun getAllCoalitions(
        token: String,
        organisation: Organisation
    ): Flow<NetworkResult<List<Coalition>>> {
        return flow {
            emit(NetworkResult.Loading())
            try{
                val request = httpClient.post("$endpoint${CoalitionRoutes.GET_ALL_COALITION}"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(organisation.toOrganisationDTO())
                }
                if(request.status == HttpStatusCode.OK) {
                    val coalitions = request.body<List<CoalitionDTO>>()
                    emit(NetworkResult.Success(coalitions.map { it.toCoalition() }))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getAllOrganisations(
        token: String,
        coalition: Coalition
    ): Flow<NetworkResult<List<Organisation>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val request = httpClient.post("$endpoint${CoalitionRoutes.GET_ALL_ORGANISATIONS}"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(coalition.toCoalitionDTO())
                }
                if(request.status == HttpStatusCode.OK) {
                    val organisations = request.body<List<OrganisationDTO>>()
                    emit(NetworkResult.Success(organisations.map { it.toOrganisation() }))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getAllRequests(
        token: String,
        organisation: Organisation
    ): Flow<NetworkResult<List<CoalitionRequest>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val request = httpClient.post("$endpoint${CoalitionRoutes.GET_COALITION_REQUEST}"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(organisation.toOrganisationDTO())
                }
                if(request.status == HttpStatusCode.OK) {
                    val coalitions = request.body<List<CoalitionRequestDTO>>()
                    CustomLog("CoalitionRequests",coalitions.toString())
                    emit(NetworkResult.Success(coalitions.map { it.toCoalitionRequest() }))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }

            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun createRequest(
        token: String,
        coalition: Coalition,
        organisation: Organisation
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint${CoalitionRoutes.CREATE_REQUEST}"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(
                        CoalitionRequestDTO(
                            coalitionId = coalition.id,
                            organisationId = organisation.id
                        )
                    )
                }
                if(response.status == HttpStatusCode.Created) {
                    emit(NetworkResult.Success(true))
                }
                else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun acceptRequest(
        token: String,
        coalitionRequest: CoalitionRequest
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint${CoalitionRoutes.ACCEPT_REQUEST}"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(coalitionRequest.coalitionRequestDTO())
                }
                if(response.status == HttpStatusCode.OK) {
                    emit(NetworkResult.Success(true))
                }
                else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun declineRequest(
        token: String,
        coalitionRequest: CoalitionRequest
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint${CoalitionRoutes.DECLINE_REQUEST}") {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(coalitionRequest.coalitionRequestDTO())
                }
                if (response.status == HttpStatusCode.OK) {
                    emit(NetworkResult.Success(true))
                }
                else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }
}
