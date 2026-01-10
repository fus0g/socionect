package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.EMU_SERVER
import dev.bugstitch.socionect.SERVER
import dev.bugstitch.socionect.WEB_SERVER
import dev.bugstitch.socionect.data.models.OrganisationSubtopicDTO
import dev.bugstitch.socionect.data.models.toOrganisationSubtopic
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
import dev.bugstitch.socionect.utils.CustomLog
import dev.bugstitch.socionect.utils.NetworkResult
import dev.bugstitch.socionect.utils.platform
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

class OrganisationSubtopicRepositoryImpl(
    private val httpClient: HttpClient
): OrganisationSubtopicRepository {

    private val endpoint =
        when (platform) {
            "android" -> EMU_SERVER
            "web" -> WEB_SERVER
            else -> SERVER
        }

    override suspend fun createSubtopic(
        subtopic: OrganisationSubtopic,
        token: String
    ): Flow<NetworkResult<OrganisationSubtopic>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                CustomLog("ERROR1",subtopic.toString())
                val response = httpClient.post("$endpoint/organisation/subtopic/create"){
                    contentType(ContentType.Application.Json)
                    setBody(OrganisationSubtopicDTO(
                        name = subtopic.name,
                        description = subtopic.description,
                        organisationId = subtopic.organisationId
                    ))
                    header("Authorization", "Bearer $token")
                }
                CustomLog("ERROR2",response.toString())
                if(response.status == HttpStatusCode.OK)
                {
                    val createdSubtopic = response.body<OrganisationSubtopicDTO>()
                    emit(NetworkResult.Success(createdSubtopic.toOrganisationSubtopic()))
                }else{
                    emit(NetworkResult.Error(response.status.description))
                }

            }catch (e: Exception){
                emit(NetworkResult.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun getAllOrganisationSubTopic(
        organisationId: String,
        token: String
    ): Flow<NetworkResult<List<OrganisationSubtopic>>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint/organisation/subtopic/get"){
                    contentType(ContentType.Application.Json)
                    setBody(OrganisationSubtopicDTO(
                        organisationId = organisationId
                    ))
                    header("Authorization", "Bearer $token")
                }
                val subtopics = response.body<List<OrganisationSubtopicDTO>>()
                emit(NetworkResult.Success(subtopics.map { it.toOrganisationSubtopic() }))

            }catch (e: Exception){
                emit(NetworkResult.Error(e.message ?: "Unknown error"))
            }
        }
    }
}