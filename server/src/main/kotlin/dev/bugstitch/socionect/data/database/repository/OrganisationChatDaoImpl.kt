package dev.bugstitch.socionect.data.database.repository

import dev.bugstitch.socionect.data.database.tables.CoalitionMessages
import dev.bugstitch.socionect.data.database.tables.OrganisationSubtopicMessages
import dev.bugstitch.socionect.data.database.tables.Users
import dev.bugstitch.socionect.domain.database.repository.OrganisationChatDao
import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage
import org.bson.types.ObjectId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class OrganisationChatDaoImpl(
    private val database: Database
): OrganisationChatDao {

    override fun getAllSubTopicChats(subtopicId: String): List<OrganisationSubtopicMessage> {
        return transaction(database) {
            (OrganisationSubtopicMessages innerJoin Users)
                .selectAll()
                .where { OrganisationSubtopicMessages.organisationSubtopicId eq subtopicId }
                .map {
                    OrganisationSubtopicMessage(
                        id = it[OrganisationSubtopicMessages.id],
                        organisationSubtopicId = it[OrganisationSubtopicMessages.organisationSubtopicId],
                        senderId = it[OrganisationSubtopicMessages.senderId],
                        senderName = it[Users.name],
                        message = it[OrganisationSubtopicMessages.message],
                        timestamp = it[OrganisationSubtopicMessages.timestamp],
                    )
                }
        }
    }

    override fun createSubtopicChat(chat: OrganisationSubtopicMessage): Boolean {
        return transaction(database) {
            OrganisationSubtopicMessages.insert {
                it[id] = ObjectId().toHexString()
                it[organisationSubtopicId] = chat.organisationSubtopicId
                it[senderId] = chat.senderId
                it[message] = chat.message
                it[timestamp] = System.currentTimeMillis()
            }.insertedCount > 0
        }
    }

    override fun getAllCoalitionChats(coalitionId: String): List<CoalitionMessage> {
        return transaction(database) {
            (CoalitionMessages innerJoin Users)
                .selectAll()
                .where{ CoalitionMessages.coalitionId eq coalitionId }.map {
                    CoalitionMessage(
                        id = it[CoalitionMessages.id],
                        senderId = it[CoalitionMessages.senderId],
                        senderName = it[Users.name],
                        message = it[CoalitionMessages.message],
                        timestamp = it[CoalitionMessages.timestamp],
                    )
                }
        }
    }

    override fun createCoalitionChat(coalitionMessage: CoalitionMessage): Boolean {
        return transaction(database) {
            CoalitionMessages.insert {
                it[id] = ObjectId().toHexString()
                it[coalitionId] = coalitionMessage.coalitionId
                it[senderId] = coalitionMessage.senderId
                it[message] = coalitionMessage.message
                it[timestamp] = System.currentTimeMillis()
            }.insertedCount > 0
        }
    }
}