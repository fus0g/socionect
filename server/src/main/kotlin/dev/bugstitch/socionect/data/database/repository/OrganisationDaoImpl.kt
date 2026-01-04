package dev.bugstitch.socionect.data.database.repository

import dev.bugstitch.socionect.data.database.tables.OrganisationJoinReceivedRequests
import dev.bugstitch.socionect.data.database.tables.OrganisationJoinSentRequests
import dev.bugstitch.socionect.data.database.tables.OrganisationMembers
import dev.bugstitch.socionect.data.database.tables.Organisations
import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationJoinReceivedRequest
import dev.bugstitch.socionect.domain.models.OrganisationJoinSentRequest
import dev.bugstitch.socionect.domain.models.OrganisationMember
import org.bson.types.ObjectId
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.collections.emptyList

class OrganisationDaoImpl(private val database: Database) : OrganisationDao {

    override fun createOrganisation(organisation: Organisation): Organisation? {

        try {
            val nOrganisation = organisation.copy(id = ObjectId().toHexString())
            var success = false
            transaction(database) {
                success = Organisations.insert {
                    it[id] = nOrganisation.id
                    it[name] = nOrganisation.name
                    it[description] = nOrganisation.description
                    it[createdAt] = System.currentTimeMillis()
                }.insertedCount > 0

            }
            return if (success) nOrganisation else null
        } catch (e: Exception) {
            return null
        }
    }

    override fun getAllUserOrganisation(userId: String): List<Organisation> {
        return try{
            transaction(database) {

                (OrganisationMembers innerJoin Organisations).selectAll()
                    .where{
                        OrganisationMembers.userId eq userId
                    }.map {
                        Organisation(
                            id = it[Organisations.id],
                            name = it[Organisations.name],
                            description = it[Organisations.description],
                            createdAt = it[Organisations.createdAt]
                        )
                    }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getAllOrganisations(): List<Organisation> {
        return try {
            transaction(database) {
                Organisations.selectAll().map {
                    Organisation(
                        id = it[Organisations.id],
                        name = it[Organisations.name],
                        description = it[Organisations.description],
                        createdAt = it[Organisations.createdAt]
                        )
                }
            }
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    override fun getOrganisationByMatchingName(name: String): List<Organisation> {
        return try {
            transaction(database) {
                Organisations.selectAll().where {
                    Organisations.name like "%$name%"
                }.map {
                    Organisation(
                        id = it[Organisations.id],
                        name = it[Organisations.name],
                        description = it[Organisations.description],
                        createdAt = it[Organisations.createdAt]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getOrganisation(id: String): Organisation? {
        return transaction(database) {
            Organisations.selectAll().where {
                Organisations.id eq id
            }.map {
                Organisation(
                    id = it[Organisations.id],
                    name = it[Organisations.name],
                    description = it[Organisations.description],
                    createdAt = it[Organisations.createdAt]
                )
            }.singleOrNull()
        }
    }

    override fun getOrganisationMember(
        organisationId: String,
        memberId: String
    ): OrganisationMember? {
        return try {
            transaction(database) {
                OrganisationMembers.selectAll().where {
                    OrganisationMembers.organisationId eq organisationId and (OrganisationMembers.userId eq memberId)
                }.map {
                    OrganisationMember(
                        id = it[OrganisationMembers.id],
                        organisationId = it[OrganisationMembers.organisationId],
                        userId = it[OrganisationMembers.userId],
                        role = it[OrganisationMembers.role],
                        joinedAt = it[OrganisationMembers.joinedAt]
                    )
                }.singleOrNull()
            }
        }catch (e: Exception){
            null
        }
    }

    override fun addOrganisationMember(
        organisationMember: OrganisationMember
    ): Boolean {
        return try {
            transaction(database) {
                OrganisationMembers.insert {
                    it[id] = ObjectId().toHexString()
                    it[organisationId] = organisationMember.organisationId
                    it[userId] = organisationMember.userId
                    it[role] = organisationMember.role
                    it[joinedAt] = System.currentTimeMillis()
                }.insertedCount > 0
        }
    }catch (e: Exception){
        return false
    }
    }

    override fun getOrganisationMembers(organisationId: String): List<OrganisationMember> {
        return try {
            transaction(database) {
                OrganisationMembers.selectAll().where {
                    OrganisationMembers.organisationId eq organisationId
                }.map {
                    OrganisationMember(
                        id = ObjectId().toHexString(),
                        organisationId = it[OrganisationMembers.organisationId],
                        userId = it[OrganisationMembers.userId],
                        role = it[OrganisationMembers.role],
                        joinedAt = it[OrganisationMembers.joinedAt]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun addOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinSentRequests.insert {
                    it[id] = ObjectId().toHexString()
                    it[organisationId] = organisationJoinSentRequest.organisationId
                    it[userId] = organisationJoinSentRequest.userId
                    it[sentAt] = System.currentTimeMillis()
                }.insertedCount > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun addOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinReceivedRequests.insert {
                    it[id] = ObjectId().toHexString()
                    it[organisationId] = organisationJoinReceivedRequest.organisationId
                    it[userId] = organisationJoinReceivedRequest.userId
                    it[receivedAt] = System.currentTimeMillis()
                }.insertedCount > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun getOrganisationJoinSentRequests(organisationId: String): List<OrganisationJoinSentRequest> {
        return try{
            transaction(database) {
                OrganisationJoinSentRequests.selectAll().where {
                    OrganisationJoinSentRequests.organisationId eq organisationId
                }.map {
                    OrganisationJoinSentRequest(
                        id = it[OrganisationJoinSentRequests.id],
                        organisationId = it[OrganisationJoinSentRequests.organisationId],
                        userId = it[OrganisationJoinSentRequests.userId],
                        sentAt = it[OrganisationJoinSentRequests.sentAt]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getOrganisationJoinReceivedRequests(organisationId: String): List<OrganisationJoinReceivedRequest> {
        return try{
            transaction(database) {
                OrganisationJoinReceivedRequests.selectAll().where {
                    OrganisationJoinReceivedRequests.organisationId eq organisationId
                }.map {
                    OrganisationJoinReceivedRequest(
                        id = it[OrganisationJoinReceivedRequests.id],
                        organisationId = it[OrganisationJoinReceivedRequests.organisationId],
                        userId = it[OrganisationJoinReceivedRequests.userId],
                        receivedAt = it[OrganisationJoinReceivedRequests.receivedAt]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getOrganisationJoinReceivedRequestsForUser(userId: String): List<OrganisationJoinReceivedRequest> {
        return try {
            transaction(database) {
                OrganisationJoinReceivedRequests.selectAll().where {
                    OrganisationJoinReceivedRequests.userId eq userId
                }.map {
                    OrganisationJoinReceivedRequest(
                        id = it[OrganisationJoinReceivedRequests.id],
                        organisationId = it[OrganisationJoinReceivedRequests.organisationId],
                        userId = it[OrganisationJoinReceivedRequests.userId],
                        receivedAt = it[OrganisationJoinReceivedRequests.receivedAt]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getOrganisationJoinSentRequestsForUser(userId: String): List<OrganisationJoinSentRequest> {
        return try {
            transaction(database) {
                OrganisationJoinSentRequests.selectAll().where {
                    OrganisationJoinSentRequests.userId eq userId
                }.map {
                    OrganisationJoinSentRequest(
                        id = it[OrganisationJoinSentRequests.id],
                        organisationId = it[OrganisationJoinSentRequests.organisationId],
                        userId = it[OrganisationJoinSentRequests.userId],
                        sentAt = it[OrganisationJoinSentRequests.sentAt]
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun deleteOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinSentRequests.deleteWhere {
                    OrganisationJoinSentRequests.id eq organisationJoinSentRequest.id
                } > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun deleteOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinReceivedRequests.deleteWhere {
                    OrganisationJoinReceivedRequests.id eq organisationJoinReceivedRequest.id
                } > 0
            }
        }catch (e: Exception){
            false
        }
    }
}