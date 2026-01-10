package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.EMU_SERVER
import dev.bugstitch.socionect.SERVER
import dev.bugstitch.socionect.WEB_SERVER
import dev.bugstitch.socionect.data.models.OrganisationDTO
import dev.bugstitch.socionect.data.models.RequestSendByOrganisationDTO
import dev.bugstitch.socionect.data.models.RequestSendByUserDTO
import dev.bugstitch.socionect.data.models.UserDTO
import dev.bugstitch.socionect.data.models.toOrganisation
import dev.bugstitch.socionect.data.models.toUser
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.utils.CustomLog
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

    override suspend fun searchOrganisation(
        query: String,
        token: String
    ): Flow<NetworkResult<List<Organisation>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint/organisation/search"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(OrganisationDTO(
                        name = query
                    ))
                }
                if(response.status == HttpStatusCode.OK)
                {
                    val orgs = response.body<List<OrganisationDTO>>()
                    emit(NetworkResult.Success(orgs.map { it.toOrganisation() }))
                }else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun userSendsRequestToOrganisation(
        organisationId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val request = httpClient.post("$endpoint/organisation/userSendRequestToOrganisation"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(RequestSendByUserDTO(
                        organisationId = organisationId,
                        ))
                }
                if(request.status == HttpStatusCode.OK)
                {
                    emit(NetworkResult.Success(true))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getAllRequestsSendByUserToOrg(token: String): Flow<NetworkResult<List<Organisation>>> {
        return flow {
            emit(NetworkResult.Loading())
            try{
                val response = httpClient.get("$endpoint/organisation/getRequestsSendByUser"){
                    header("Authorization", "Bearer $token")
                }
                if(response.status == HttpStatusCode.OK)
                {
                    val orgs = response.body<List<OrganisationDTO>>()
                    CustomLog("ORGS",orgs.toString())
                    emit(NetworkResult.Success(orgs.map { it.toOrganisation() }))
                }else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getRequestsReceivedByUser(token: String): Flow<NetworkResult<List<Organisation>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val request = httpClient.get("$endpoint/organisation/getRequestsReceivedByUser"){
                    header("Authorization", "Bearer $token")
                }
                if(request.status == HttpStatusCode.OK)
                {
                    val orgs = request.body<List<OrganisationDTO>>()
                    emit(NetworkResult.Success(orgs.map { it.toOrganisation() }))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }

            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun userAcceptsRequest(
        organisationId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val request = httpClient.post("$endpoint/organisation/userConfirmRequestReceivedFromOrganisation"){
                    contentType(ContentType.Application.Json)
                    header("Authorization", "Bearer $token")
                    setBody(RequestSendByOrganisationDTO(
                        organisationId = organisationId
                    )
                    )
                }
                if(request.status == HttpStatusCode.OK)
                {
                    emit(NetworkResult.Success(true))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun userDeclinesRequest(
        organisationId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val request =
                    httpClient.post("$endpoint/organisation/userDeclineRequestFromOrganisation") {
                        contentType(ContentType.Application.Json)
                        header("Authorization", "Bearer $token")
                        setBody(
                            RequestSendByOrganisationDTO(
                                organisationId = organisationId
                            )
                        )
                    }

                if (request.status == HttpStatusCode.OK) {
                    emit(NetworkResult.Success(true))
                } else {
                    emit(NetworkResult.Error(request.status.description))
                }
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getRequestsReceivedByOrganisation(
        organisationId: String,
        token: String
    ): Flow<NetworkResult<List<User>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint/organisation/getRequestsReceivedByOrganisation"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(OrganisationDTO(
                        id = organisationId
                    ))
                }
                if(response.status == HttpStatusCode.OK)
                {
                    val users = response.body<List<UserDTO>>()
                    emit(NetworkResult.Success(users.map { it.toUser() }))
                }else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun organisationAcceptsRequest(
        userId: String,
        organisationId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try{
                val request = httpClient.post("$endpoint/organisation/organisationConfirmsRequestByUser"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(RequestSendByUserDTO(
                        userId = userId,
                        organisationId = organisationId
                    ))
                }

                if(request.status == HttpStatusCode.OK)
                {
                    emit(NetworkResult.Success(true))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }
            }catch (e:Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun organisationDeclinesRequest(
        userId: String,
        organisationId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try{
                val request = httpClient.post("$endpoint/organisation/requestReceivedFromUserDeclinedByOrganisation"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(RequestSendByUserDTO(
                        userId = userId,
                        organisationId = organisationId
                    ))
                }
                if(request.status == HttpStatusCode.OK)
                {
                    emit(NetworkResult.Success(true))
                }else{
                    emit(NetworkResult.Error(request.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }


    override suspend fun getAllMembers(
        organisationId: String,
        token: String
    ): Flow<NetworkResult<List<User>>> {
        return flow {
            emit(NetworkResult.Loading())
            try{
                val response = httpClient.post("$endpoint/organisation/getAllMembers"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(OrganisationDTO(
                        id = organisationId
                    ))
                }
                if(response.status == HttpStatusCode.OK)
                {
                    val users = response.body<List<UserDTO>>()
                    emit(NetworkResult.Success(users.map { it.toUser() }))
                }else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun getRequestsSendToUser(
        organisationId: String,
        token: String
    ): Flow<NetworkResult<List<User>>> {
        return flow {
            emit(NetworkResult.Loading())
            try{
                val response = httpClient.post("$endpoint/organisation/getRequestsSendToUser"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(OrganisationDTO(
                        id = organisationId
                    ))
                }
                if(response.status == HttpStatusCode.OK)
                {
                    val users = response.body<List<UserDTO>>()
                    emit(NetworkResult.Success(users.map { it.toUser() }))
                }else{
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun sendRequestToUser(
        userId: String,
        organisationId: String,
        token: String
    ): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint/organisation/sendRequestToUser"){
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(RequestSendByOrganisationDTO(
                        userId = userId,
                        organisationId = organisationId
                    ))
                }
                if(response.status == HttpStatusCode.OK)
                {
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