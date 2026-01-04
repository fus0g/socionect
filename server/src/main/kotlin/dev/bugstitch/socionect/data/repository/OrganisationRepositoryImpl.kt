package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationJoinReceivedRequest
import dev.bugstitch.socionect.domain.models.OrganisationJoinSentRequest
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.repository.OrganisationRepository

class OrganisationRepositoryImpl(private val organisationDao: OrganisationDao): OrganisationRepository {

    override fun createOrganisation(organisation: Organisation, userId: String): Organisation? {
        try{
            val nOrganisation = organisationDao.createOrganisation(organisation) ?: return null
            val success = organisationDao.addOrganisationMember(
                OrganisationMember(
                    id = "",
                    organisationId = nOrganisation.id,
                    userId = userId,
                    role = 0,
                    joinedAt = System.currentTimeMillis()
                )
            )
            return if (success) nOrganisation else null
        }catch (e: Exception){
            return null
        }
    }

    override fun getUserOrganisations(userId: String): List<Organisation> {
        return organisationDao.getAllUserOrganisation(userId)
    }

    override fun getOrganisationsByMatchingName(name: String): List<Organisation> {
        return organisationDao.getOrganisationByMatchingName(name)
    }

    override fun getOrganisation(id: String): Organisation? {
        return organisationDao.getOrganisation(id)
    }

    override fun addOrganisationMember(organisationMember: OrganisationMember,currentUserId: String): Boolean {
        val user = organisationDao.getOrganisationMember(organisationMember.organisationId,currentUserId)
        if(user == null || user.role > 1) return false
        return organisationDao.addOrganisationMember(organisationMember)
    }

    override fun getOrganisationMembers(organisationId: String): List<OrganisationMember> {
        return organisationDao.getOrganisationMembers(organisationId)
    }

    override fun getAllOrganisationJoinSentRequests(organisationId: String): List<OrganisationJoinSentRequest> {
        return try {
            organisationDao.getOrganisationJoinSentRequests(organisationId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getAllOrganisationJoinReceivedRequests(organisationId: String): List<OrganisationJoinReceivedRequest> {
        return try {
            organisationDao.getOrganisationJoinReceivedRequests(organisationId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun confirmOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest): Boolean {
        return try {
            val exist = organisationDao.getOrganisationMember(organisationJoinSentRequest.organisationId,organisationJoinSentRequest.userId)
            if(exist != null) return false
            val success = organisationDao.addOrganisationMember(
                OrganisationMember(
                    id = "",
                    organisationId = organisationJoinSentRequest.organisationId,
                    userId = organisationJoinSentRequest.userId,
                    role = 0,
                    joinedAt = System.currentTimeMillis()
                )
            )
            organisationDao.deleteOrganisationJoinSentRequest(organisationJoinSentRequest)
            success
        } catch (e: Exception) {
            false
        }
    }

    override fun declineOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest): Boolean {
        return try {
            organisationDao.deleteOrganisationJoinSentRequest(organisationJoinSentRequest)
        }catch (e: Exception){
            false
        }
    }

    override fun confirmOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest,currentUserId: String): Boolean {
        return try {
            val user = organisationDao.getOrganisationMember(organisationJoinReceivedRequest.organisationId,currentUserId)
            if(user == null || user.role > 1) return false
            val exist = organisationDao.getOrganisationMember(organisationJoinReceivedRequest.organisationId,organisationJoinReceivedRequest.userId)
            if(exist != null) return false
            val success = organisationDao.addOrganisationMember(
                OrganisationMember(
                    id = "",
                    organisationId = organisationJoinReceivedRequest.organisationId,
                    userId = organisationJoinReceivedRequest.userId,
                    role = 0,
                    joinedAt = System.currentTimeMillis()
                )
            )
            organisationDao.deleteOrganisationJoinReceivedRequest(organisationJoinReceivedRequest)
            success
        } catch (e: Exception) {
            false
        }
    }

    override fun declineOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest,currentUserId: String): Boolean {
        return try {
            val user = organisationDao.getOrganisationMember(organisationJoinReceivedRequest.organisationId,currentUserId)
            if(user == null || user.role > 1) return false
            organisationDao.deleteOrganisationJoinReceivedRequest(organisationJoinReceivedRequest)
        }catch (e: Exception){
            false
        }
    }

    override fun sendOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest,currentUserId: String): Boolean {
        return try {
            val user = organisationDao.getOrganisationMember(organisationJoinSentRequest.organisationId,currentUserId)
            if(user == null || user.role > 1) return false
            val exist = organisationDao.getOrganisationMember(organisationJoinSentRequest.organisationId,organisationJoinSentRequest.userId)
            if(exist != null) return false
            organisationDao.addOrganisationJoinSentRequest(organisationJoinSentRequest)
        }catch (e: Exception){
            false
        }
    }

    override fun sendOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest): Boolean {
        return try {
            val exist = organisationDao.getOrganisationMember(organisationJoinReceivedRequest.organisationId,organisationJoinReceivedRequest.userId)
            if(exist != null) return false
            organisationDao.addOrganisationJoinReceivedRequest(organisationJoinReceivedRequest)
            }catch (e: Exception){
            false
        }
    }

    override fun getOrganisationJoinSentRequestsForUser(userId: String): List<OrganisationJoinSentRequest> {
        return try {
            organisationDao.getOrganisationJoinSentRequestsForUser(userId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getOrganisationJoinReceivedRequestsForUser(userId: String): List<OrganisationJoinReceivedRequest> {
        return try {
            organisationDao.getOrganisationJoinReceivedRequestsForUser(userId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}