package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface OrganisationSubtopicRepository {

    suspend fun createSubtopic(subtopic: OrganisationSubtopic, token: String): Flow<NetworkResult<OrganisationSubtopic>>

    suspend fun getAllOrganisationSubTopic(organisationId: String, token: String): Flow<NetworkResult<List<OrganisationSubtopic>>>
}