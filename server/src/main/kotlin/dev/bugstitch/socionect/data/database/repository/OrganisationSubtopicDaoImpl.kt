package dev.bugstitch.socionect.data.database.repository

import dev.bugstitch.socionect.data.database.tables.OrganisationSubtopicMembers
import dev.bugstitch.socionect.data.database.tables.OrganisationSubtopicMessages
import dev.bugstitch.socionect.data.database.tables.OrganisationSubtopics
import dev.bugstitch.socionect.domain.database.repository.OrganisationSubtopicDao
import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMember
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import org.bson.types.ObjectId
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class OrganisationSubtopicDaoImpl(private val database: Database) : OrganisationSubtopicDao {

    override fun createSubtopic(orgSubtopic: OrganisationSubtopic): OrganisationSubtopic? {
        return try {
            val nSubtopic = orgSubtopic.copy(
                id = ObjectId().toHexString(),
                createdAt = System.currentTimeMillis()
            )

            val success = transaction(database) {
                OrganisationSubtopics.insert {
                    it[id] = nSubtopic.id
                    it[name] = nSubtopic.name
                    it[description] = nSubtopic.description
                    it[organisationId] = nSubtopic.organisationId
                    it[createdAt] = nSubtopic.createdAt
                }.insertedCount > 0
            }

            if (success) {
                nSubtopic
            } else {
                null
            }

        }catch (e: Exception)
        {
            null
        }
    }

    override fun getAllSubtopics(orgId: String): List<OrganisationSubtopic> {
        return try {
            transaction(database) {
                OrganisationSubtopics.selectAll().where {
                    OrganisationSubtopics.organisationId eq orgId
                }.map {
                    OrganisationSubtopic(
                        id = it[OrganisationSubtopics.id],
                        name = it[OrganisationSubtopics.name],
                        description = it[OrganisationSubtopics.description],
                        organisationId = it[OrganisationSubtopics.organisationId],
                        createdAt = it[OrganisationSubtopics.createdAt]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getSubtopic(id: String): OrganisationSubtopic? {
        return try {
            transaction(database) {
                OrganisationSubtopics.selectAll().where {
                    OrganisationSubtopics.id eq id
                }.map {
                    OrganisationSubtopic(
                        id = it[OrganisationSubtopics.id],
                        name = it[OrganisationSubtopics.name],
                        description = it[OrganisationSubtopics.description],
                        organisationId = it[OrganisationSubtopics.organisationId],
                        createdAt = it[OrganisationSubtopics.createdAt]
                    )
                }.singleOrNull()
            }
        }catch (e: Exception){
            null
        }
    }

    override fun deleteSubtopic(id: String): Boolean {
        return try {
            transaction(database) {
                OrganisationSubtopics.deleteWhere {
                    OrganisationSubtopics.id eq id
                } > 0
            }
        }catch (e: Exception) {
            false
        }
    }

    override fun addSubTopicMember(subtopicMember: OrganisationSubtopicMember): Boolean {
        return try {
            transaction(database) {
                OrganisationSubtopicMembers.insert {
                    it[id] = ObjectId().toHexString()
                    it[organisationSubtopicId] = subtopicMember.organisationSubtopicId
                    it[userId] = subtopicMember.userId
                    it[joinedAt] = System.currentTimeMillis()
                    it[isAdmin] = subtopicMember.isAdmin
                    it[canSendMessage] = subtopicMember.canSendMessage
                }.insertedCount > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun getSubtopicMembers(subtopicId: String): List<OrganisationSubtopicMember> {
        return try {
            transaction(database) {
                OrganisationSubtopicMembers.selectAll().where {
                    OrganisationSubtopicMembers.organisationSubtopicId eq subtopicId
                }.map {
                    OrganisationSubtopicMember(
                        id = it[OrganisationSubtopicMembers.id],
                        organisationSubtopicId = it[OrganisationSubtopicMembers.organisationSubtopicId],
                        userId = it[OrganisationSubtopicMembers.userId],
                        joinedAt = it[OrganisationSubtopicMembers.joinedAt],
                        isAdmin = it[OrganisationSubtopicMembers.isAdmin],
                        canSendMessage = it[OrganisationSubtopicMembers.canSendMessage]
                    )
                }
            }
        }catch (e: Exception) {
            emptyList()
        }
    }

    override fun getSubtopicMember(
        subtopicId: String,
        userId: String
    ): OrganisationSubtopicMember? {
        return try {
            transaction(database) {
                OrganisationSubtopicMembers.selectAll().where {
                    OrganisationSubtopicMembers.organisationSubtopicId eq subtopicId and (OrganisationSubtopicMembers.userId eq userId)
                }.map {
                    OrganisationSubtopicMember(
                        id = it[OrganisationSubtopicMembers.id],
                        organisationSubtopicId = it[OrganisationSubtopicMembers.organisationSubtopicId],
                        userId = it[OrganisationSubtopicMembers.userId],
                        joinedAt = it[OrganisationSubtopicMembers.joinedAt],
                        isAdmin = it[OrganisationSubtopicMembers.isAdmin],
                        canSendMessage = it[OrganisationSubtopicMembers.canSendMessage]
                    )
                }.singleOrNull()
            }
        }catch (e: Exception){
            null
        }
    }

    override fun removeSubTopicMember(
        subtopicId: String,
        userId: String
    ): Boolean {
        return try {
            transaction(database) {
                OrganisationSubtopicMembers.deleteWhere {
                    OrganisationSubtopicMembers.organisationSubtopicId eq subtopicId and (OrganisationSubtopicMembers.userId eq userId)
                } > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun updateSubTopicMember(subtopicMember: OrganisationSubtopicMember): Boolean {
        return try {
            transaction(database) {
                OrganisationSubtopicMembers.update({
                    OrganisationSubtopicMembers.id eq subtopicMember.id
                }) {
                    it[isAdmin] = subtopicMember.isAdmin
                    it[canSendMessage] = subtopicMember.canSendMessage
                    } > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun addMessage(
        subtopicId: String,
        message: String,
        senderId: String
    ): Boolean {
        return try {
            transaction(database) {
                OrganisationSubtopicMessages.insert {
                    it[id] = ObjectId().toHexString()
                    it[organisationSubtopicId] = subtopicId
                    it[this.senderId] = senderId
                    it[this.message] = message
                    it[timestamp] = System.currentTimeMillis()
                }.insertedCount > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun getMessages(subtopicId: String): List<OrganisationSubtopicMessage> {
        return try {
            transaction(database) {
                OrganisationSubtopicMessages.selectAll().where {
                    OrganisationSubtopicMessages.organisationSubtopicId eq subtopicId
                }.map {
                    OrganisationSubtopicMessage(
                        id = it[OrganisationSubtopicMessages.id],
                        organisationSubtopicId = it[OrganisationSubtopicMessages.organisationSubtopicId],
                        senderId = it[OrganisationSubtopicMessages.senderId],
                        message = it[OrganisationSubtopicMessages.message],
                        timestamp = it[OrganisationSubtopicMessages.timestamp]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun removeAllSubtopicMembers(subtopicId: String): Boolean {
        return try {
            transaction(database) {
                OrganisationSubtopicMembers.deleteWhere {
                    OrganisationSubtopicMembers.organisationSubtopicId eq subtopicId
                } > 0
            }
        }catch (e: Exception){
            false
        }
    }
}