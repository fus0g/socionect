package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.CoalitionDao
import dev.bugstitch.socionect.domain.database.repository.OrganisationChatDao
import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.database.repository.OrganisationSubtopicDao
import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import dev.bugstitch.socionect.domain.repository.OrganisationChatRepository

class OrganisationChatRepositoryImpl(
    private val organisationChatDao: OrganisationChatDao,
    private val organisationDao: OrganisationDao,
    private val subtopicDao: OrganisationSubtopicDao,
    private val coalitionDao: CoalitionDao
): OrganisationChatRepository {

    override fun getAllSubTopicChats(
        userId: String,
        subtopicId: String
    ): List<OrganisationSubtopicMessage> {
        try {
            val subtopic = subtopicDao.getSubtopic(subtopicId) ?: return emptyList()
            val org = organisationDao.getOrganisation(subtopic.organisationId) ?: return emptyList()
            val user = organisationDao.getOrganisationMember(org.id, userId) ?: return emptyList()
            return organisationChatDao.getAllSubTopicChats(subtopicId)
        }catch (e:Exception){
            return emptyList()
        }

    }

    override fun createSubTopicMessage(
        userId: String,
        organisationSubtopicMessage: OrganisationSubtopicMessage
    ): Boolean {
        try {
            val subtopic = subtopicDao.getSubtopic(organisationSubtopicMessage.organisationSubtopicId) ?: return false
            val org = organisationDao.getOrganisation(subtopic.organisationId) ?: return false
            val user = organisationDao.getOrganisationMember(org.id, userId) ?: return false

            return organisationChatDao.createSubtopicChat(organisationSubtopicMessage.copy(senderId = userId))

        }catch (e:Exception){
            return false
        }
    }

    override fun getAllCoalitionChats(
        userId: String,
        coalitionId: String
    ): List<CoalitionMessage> {
        try {
            val orgs = coalitionDao.getAllCoalitionOrganizations(coalitionId)
            var exists = false
            orgs.forEach { org ->
                if(organisationDao.getOrganisationMember(org.id, userId) != null) {
                    exists = true
                }
            }
            if (!exists) {
                return emptyList()
            }
            return organisationChatDao.getAllCoalitionChats(coalitionId)
        }catch (e:Exception){
            return emptyList()
        }
    }

    override fun createCoalitionMessage(
        userId: String,
        message: CoalitionMessage
    ): Boolean {
        try {
            val orgs = coalitionDao.getAllCoalitionOrganizations(message.coalitionId)
            var exists = false
            orgs.forEach { org ->
                if(organisationDao.getOrganisationMember(org.id, userId) != null) {
                    exists = true
                }
            }
            if (!exists) {
                return false
            }
            return organisationChatDao.createCoalitionChat(message.copy(senderId = userId))
        }catch (e:Exception){
            return false
        }
    }
}