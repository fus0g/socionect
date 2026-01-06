package dev.bugstitch.socionect.data.database.repository

import dev.bugstitch.socionect.data.database.tables.OrganisationJoinRequestByUser
import dev.bugstitch.socionect.data.database.tables.OrganisationJoinRequestByOrganisation
import dev.bugstitch.socionect.data.database.tables.OrganisationMembers
import dev.bugstitch.socionect.data.database.tables.Organisations
import dev.bugstitch.socionect.data.database.tables.Users
import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.RequestSendByUser
import dev.bugstitch.socionect.domain.models.RequestSendByOrganisation
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.models.User
import org.bson.types.ObjectId
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
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

    override fun getOrganisationMembers(organisationId: String): List<User> {
        return try {
            transaction(database) {
                (OrganisationMembers innerJoin Users).selectAll().where {
                    OrganisationMembers.organisationId eq organisationId
                }.map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        email = it[Users.email],
                        username = it[Users.username],
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun addRequestSendByOrganisation(requestSendByOrganisation: RequestSendByOrganisation): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinRequestByOrganisation.insert {
                    it[id] = ObjectId().toHexString()
                    it[organisationId] = requestSendByOrganisation.organisationId
                    it[userId] = requestSendByOrganisation.userId
                    it[sentAt] = System.currentTimeMillis()
                }.insertedCount > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun addRequestsSendByUser(requestSendByUser: RequestSendByUser): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinRequestByUser.insert {
                    it[id] = ObjectId().toHexString()
                    it[organisationId] = requestSendByUser.organisationId
                    it[userId] = requestSendByUser.userId
                    it[receivedAt] = System.currentTimeMillis()
                }.insertedCount > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun getOrganisationJoinRequestSendByOrganisation(organisationId: String): List<User> {
        return try{
            transaction(database) {
                (OrganisationJoinRequestByOrganisation innerJoin Users).selectAll().where {
                    OrganisationJoinRequestByOrganisation.organisationId eq organisationId
                }.map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        email = it[Users.email],
                        username = it[Users.username],
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getOrganisationJoinRequestReceivedByOrganisation(organisationId: String): List<User> {
        return try{
            transaction(database) {
                (OrganisationJoinRequestByUser innerJoin Users).selectAll().where {
                    OrganisationJoinRequestByUser.organisationId eq organisationId
                }.map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        email = it[Users.email],
                        username = it[Users.username],
                    )
                }
            }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getOrganisationJoinRequestsSendByUser(userId: String): List<Organisation> {
        return try {
            transaction(database) {
                (OrganisationJoinRequestByUser innerJoin Organisations).selectAll().where {
                    OrganisationJoinRequestByUser.userId eq userId
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

    override fun getOrganisationJoinRequestsReceivedByUser(userId: String): List<Organisation> {
        return try {
            transaction(database) {
                (OrganisationJoinRequestByOrganisation innerJoin Organisations).selectAll().where {
                    OrganisationJoinRequestByOrganisation.userId eq userId
                }.map {
                    Organisation(
                        id = it[OrganisationJoinRequestByOrganisation.id],
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

    override fun deleteOrganisationJoinRequestSendByOrganisation(requestSendByOrganisation: RequestSendByOrganisation): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinRequestByOrganisation.deleteWhere {
                    (OrganisationJoinRequestByOrganisation.organisationId eq requestSendByOrganisation.organisationId) and
                            (OrganisationJoinRequestByOrganisation.userId eq requestSendByOrganisation.userId)
                } > 0
            }
        }catch (e: Exception){
            false
        }
    }

    override fun deleteOrganisationJoinRequestSentByUser(requestSendByUser: RequestSendByUser): Boolean {
        return try {
            transaction(database) {
                OrganisationJoinRequestByUser.deleteWhere {
                    (OrganisationJoinRequestByUser.organisationId eq requestSendByUser.organisationId) and
                            (OrganisationJoinRequestByUser.userId eq requestSendByUser.userId)
                } > 0
            }
        }catch (e: Exception){
            false
        }
    }
}