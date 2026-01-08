package dev.bugstitch.socionect.data.database.repository

import dev.bugstitch.socionect.data.database.tables.CoalitionMessages.coalitionId
import dev.bugstitch.socionect.data.database.tables.CoalitionOrganisations
import dev.bugstitch.socionect.data.database.tables.CoalitionRequests
import dev.bugstitch.socionect.data.database.tables.Coalitions
import dev.bugstitch.socionect.data.database.tables.Coalitions.createdAt
import dev.bugstitch.socionect.data.database.tables.Coalitions.description
import dev.bugstitch.socionect.data.database.tables.Coalitions.name
import dev.bugstitch.socionect.data.database.tables.OrganisationJoinRequestByOrganisation.organisationId
import dev.bugstitch.socionect.data.database.tables.OrganisationMembers.joinedAt
import dev.bugstitch.socionect.data.database.tables.Organisations
import dev.bugstitch.socionect.domain.database.repository.CoalitionDao
import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.CoalitionOrganisation
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation
import org.bson.types.ObjectId
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class CoalitionDaoImpl(
    private val database: Database
): CoalitionDao {

    override fun addCoalition(coalition: Coalition): Coalition? {
        val nCoalition = coalition.copy(
            id = ObjectId().toHexString(),
            createdAt = System.currentTimeMillis(),
        )
        val created = transaction(database){
            Coalitions.insert {
                it[id] = nCoalition.id
                it[name] = nCoalition.name
                it[description] = nCoalition.description
                it[createdAt] = nCoalition.createdAt
            }.insertedCount > 0
        }
        return if (created) {
            nCoalition
        }else null
    }

    override fun getCoalition(id: String): Coalition? {
        return transaction(database) {
            Coalitions.selectAll().where {
                Coalitions.id eq id
            }.map {
                Coalition(
                    id = it[Coalitions.id],
                    name = it[name],
                    description = it[description],
                    createdAt = it[createdAt]
                )
            }.singleOrNull()
        }
    }

    override fun addOrganisationToCoalition(coalitionOrganisation: CoalitionOrganisation): CoalitionOrganisation? {
        val coalitionOrganisation = coalitionOrganisation.copy(
            id = ObjectId().toHexString(),
            joinedAt = System.currentTimeMillis(),
        )
        val created = transaction(database) {
            CoalitionOrganisations.insert {
                it[id] = coalitionOrganisation.id
                it[coalitionId] = coalitionOrganisation.coalitionId
                it[organisationId] = coalitionOrganisation.organisationId
                it[joinedAt] = coalitionOrganisation.joinedAt
            }.insertedCount > 0
        }

        return if (created) {
            coalitionOrganisation
        }else null

    }

    override fun getAllCoalitionOrganizations(coalitionId: String): List<Organisation> {
        return transaction(database) {
            (CoalitionOrganisations innerJoin Organisations).selectAll().where{
                CoalitionOrganisations.coalitionId eq coalitionId
            }.map {
                Organisation(
                    id = it[Organisations.id],
                    name = it[Organisations.name],
                    description = it[Organisations.description],
                    createdAt = it[Organisations.createdAt],
                )
            }
        }
    }

    override fun getAllOrganisationCoalitions(id: String): List<Coalition> {
        return transaction(database){
            (Coalitions innerJoin CoalitionOrganisations)
                .selectAll()
                .where{
                    CoalitionOrganisations.organisationId eq id
                }.map {
                    Coalition(
                        id = it[Coalitions.id],
                        name = it[Coalitions.name],
                        description = it[Coalitions.description],
                        createdAt = it[Coalitions.createdAt],
                    )
                }
        }
    }

    override fun getAllCoalitionRequests(organisationId: String): List<CoalitionRequest> {
        return transaction(database) {
            (CoalitionRequests innerJoin Coalitions).selectAll()
                .where{
                CoalitionRequests.organisationId eq organisationId
            }.map {
                CoalitionRequest(
                    id = it[CoalitionRequests.id],
                    organisationId = it[CoalitionRequests.organisationId],
                    coalitionId = it[CoalitionRequests.coalitionId],
                    sentAt = it[CoalitionRequests.sentAt],
                    coalition = Coalition(
                        id = it[Coalitions.id],
                        name = it[Coalitions.name],
                        description = it[Coalitions.description],
                        createdAt = it[Coalitions.createdAt]
                    )
                )
            }
        }
    }

    override fun createCoalitionRequest(coalitionRequest: CoalitionRequest): CoalitionRequest? {
        val nCoalitionRequest = coalitionRequest.copy(
            id = ObjectId().toHexString(),
            sentAt = System.currentTimeMillis(),
        )

        val created = transaction(database){
            CoalitionRequests.insert {
                it[id] = nCoalitionRequest.id
                it[coalitionId] = nCoalitionRequest.coalitionId
                it[organisationId] = nCoalitionRequest.organisationId
                it[sentAt] = nCoalitionRequest.sentAt
            }.insertedCount > 0
        }

        return if (created) {
            nCoalitionRequest
        }else null

    }

    override fun deleteCoalitionRequest(coalitionRequest: CoalitionRequest): Boolean {
        return transaction(database) {
            CoalitionRequests.deleteWhere {
                CoalitionRequests.id eq coalitionRequest.id
            }
        } > 0
    }
}