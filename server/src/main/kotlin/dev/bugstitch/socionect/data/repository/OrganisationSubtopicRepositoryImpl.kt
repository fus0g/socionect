package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.database.repository.OrganisationSubtopicDao
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMember
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository

class OrganisationSubtopicRepositoryImpl(private val organisationSubtopicDao: OrganisationSubtopicDao,
    private val organisationDao: OrganisationDao) : OrganisationSubtopicRepository {

    override fun createSubtopic(
        orgSubtopic: OrganisationSubtopic,
        userId: String
    ): OrganisationSubtopic? {
        try {
            val user = organisationDao.getOrganisationMember(orgSubtopic.organisationId,userId)?: return null
            if (user.role  <= 1) {
                val nSubtopic  = organisationSubtopicDao.createSubtopic(orgSubtopic)?: return null
                organisationSubtopicDao.addSubTopicMember(
                    OrganisationSubtopicMember(
                        id = "",
                        organisationSubtopicId = nSubtopic.id,
                        userId = userId,
                        joinedAt = System.currentTimeMillis(),
                        isAdmin = true,
                        canSendMessage = true
                    )
                )
                return nSubtopic
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

    override fun getAllSubtopics(orgId: String): List<OrganisationSubtopic> {
        return organisationSubtopicDao.getAllSubtopics(orgId)
    }

    override fun getSubtopic(id: String): OrganisationSubtopic? {
        return organisationSubtopicDao.getSubtopic(id)
    }

    override fun deleteSubtopic(id: String, userId: String): Boolean {
        try {
            val user = organisationSubtopicDao.getSubtopicMember(id, userId) ?: return false
            if (user.isAdmin) {
                organisationSubtopicDao.removeAllSubtopicMembers(id)
                return organisationSubtopicDao.deleteSubtopic(id)
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    override fun addSubTopicMember(
        subtopicMember: OrganisationSubtopicMember,
        currentUserId: String
    ): Boolean {
        try {
            val user = organisationSubtopicDao.getSubtopicMember(subtopicMember.organisationSubtopicId, currentUserId) ?: return false
            if (user.isAdmin) {
                return organisationSubtopicDao.addSubTopicMember(subtopicMember)
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    override fun getSubtopicMembers(subtopicId: String): List<OrganisationSubtopicMember> {
        return organisationSubtopicDao.getSubtopicMembers(subtopicId)
    }

    override fun getSubtopicMember(
        subtopicId: String,
        userId: String
    ): OrganisationSubtopicMember? {
        return organisationSubtopicDao.getSubtopicMember(subtopicId, userId)
    }

    override fun removeSubTopicMember(
        subtopicId: String,
        userId: String
    ): Boolean {
        try {
            val user = organisationSubtopicDao.getSubtopicMember(subtopicId, userId) ?: return false
            if (user.isAdmin) {
                return organisationSubtopicDao.removeSubTopicMember(subtopicId, userId)
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    override fun updateSubTopicMember(subtopicMember: OrganisationSubtopicMember,userId: String): Boolean {
        try {
            val user = organisationSubtopicDao.getSubtopicMember(subtopicMember.organisationSubtopicId, userId) ?: return false
            if (user.isAdmin) {
                return organisationSubtopicDao.updateSubTopicMember(subtopicMember)
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    override fun sendMessage(
        subtopicId: String,
        message: String,
        senderId: String
    ): Boolean {
        try {
            val user = organisationSubtopicDao.getSubtopicMember(subtopicId, senderId) ?: return false
            if (user.canSendMessage) {
                return organisationSubtopicDao.addMessage(subtopicId, message, senderId)
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    override fun getMessages(subtopicId: String): List<OrganisationSubtopicMessage> {
        return organisationSubtopicDao.getMessages(subtopicId)
    }

    override fun removeAllSubtopicMembers(subtopicId: String,userId: String): Boolean {
        try {
            val user = organisationSubtopicDao.getSubtopicMember(subtopicId, userId) ?: return false
            if (user.isAdmin) {
                return organisationSubtopicDao.removeAllSubtopicMembers(subtopicId)
            }
            return false

        } catch (e: Exception) {
            return false
        }
    }
}