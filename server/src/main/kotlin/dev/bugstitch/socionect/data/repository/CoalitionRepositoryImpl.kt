package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.CoalitionDao
import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.CoalitionOrganisation
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.repository.CoalitionRepository

class CoalitionRepositoryImpl(
    private val coalitionDao: CoalitionDao,
    private val organisationDao: OrganisationDao
) : CoalitionRepository {

    override fun createCoalition(coalition: Coalition, hostOrg: Organisation, userId: String, list: List<Organisation>): Boolean {
        try {
            val user = organisationDao.getOrganisationMember(hostOrg.id,userId) ?: return false
            if (user.role > 1) return false

            val nCoalition = coalitionDao.addCoalition(coalition) ?: return false
            coalitionDao.addOrganisationToCoalition(
                CoalitionOrganisation(
                    id = "",
                    coalitionId = nCoalition.id,
                    organisationId = hostOrg.id,
                    joinedAt = 0
                )
            ) ?: return false

            list.forEach {
                coalitionDao.createCoalitionRequest(CoalitionRequest(
                    id = "",
                    coalitionId = nCoalition.id,
                    organisationId = it.id,
                    sentAt = 0
                ))

            }

            return true
        }catch (e: Exception){
            return false
        }

    }

    override fun getAllOrganisationCoalition(id: String, userId: String): List<Coalition> {

        try {
            organisationDao.getOrganisationMember(id,userId)?: return emptyList()

            return coalitionDao.getAllOrganisationCoalitions(id)

        }catch (e: Exception){
            return emptyList()
        }
    }

    override fun getAllOrganisationsInCoalition(id: String): List<Organisation> {
        return try {
            coalitionDao.getAllCoalitionOrganizations(id)
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun getCoalition(id: String): Coalition? {
        return try {
            coalitionDao.getCoalition(id)
        }catch (e: Exception){
            null
        }
    }

    override fun createCoalitionRequest(
        request: CoalitionRequest,
        userId: String
    ): Boolean {
        return try {
            coalitionDao.createCoalitionRequest(
                request
            ) != null
        }catch (e: Exception){
            false
        }
    }

    override fun acceptCoalitionRequest(
        request: CoalitionRequest,
        userId: String
    ): Boolean {
        try {

           val added = coalitionDao.addOrganisationToCoalition(CoalitionOrganisation(
                id = "",
                coalitionId = request.coalitionId,
                organisationId = request.organisationId,
                joinedAt = 0
            )) ?: return false

            return coalitionDao.deleteCoalitionRequest(request)


        }catch (e:Exception){
            return false
        }
    }

    override fun declineCoalitionRequest(
        request: CoalitionRequest,
        userId: String
    ): Boolean {
        return try {
            coalitionDao.deleteCoalitionRequest(request)
        }catch (e: Exception){
            return false
        }
    }

    override fun getAllCoalitionRequests(orgId: String): List<CoalitionRequest> {
        return try {
            coalitionDao.getAllCoalitionRequests(orgId)
        }catch (e: Exception){
            emptyList()
        }
    }
}