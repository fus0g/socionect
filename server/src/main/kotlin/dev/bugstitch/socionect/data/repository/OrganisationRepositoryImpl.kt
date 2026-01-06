package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.RequestSendByUser
import dev.bugstitch.socionect.domain.models.RequestSendByOrganisation
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.models.User
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

    override fun getOrganisationMembers(organisationId: String): List<User> {
        return organisationDao.getOrganisationMembers(organisationId)
    }

    override fun getAllRequestsSendByOrganisation(organisationId: String): List<User> {
        return try {
            organisationDao.getOrganisationJoinRequestSendByOrganisation(organisationId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getAllRequestsReceivedByOrganisation(organisationId: String): List<User> {
        return try {
            organisationDao.getOrganisationJoinRequestReceivedByOrganisation(organisationId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun requestConfirmedByUser(requestSendByOrganisation: RequestSendByOrganisation): Boolean {
        return try {
            val exist = organisationDao.getOrganisationMember(requestSendByOrganisation.organisationId,requestSendByOrganisation.userId)
            if(exist != null) return false
            val success = organisationDao.addOrganisationMember(
                OrganisationMember(
                    id = "",
                    organisationId = requestSendByOrganisation.organisationId,
                    userId = requestSendByOrganisation.userId,
                    role = 0,
                    joinedAt = System.currentTimeMillis()
                )
            )
            organisationDao.deleteOrganisationJoinRequestSendByOrganisation(requestSendByOrganisation)
            success
        } catch (e: Exception) {
            false
        }
    }

    override fun requestDeclinedByUser(requestSendByOrganisation: RequestSendByOrganisation): Boolean {
        return try {
            organisationDao.deleteOrganisationJoinRequestSendByOrganisation(requestSendByOrganisation)
        }catch (e: Exception){
            false
        }
    }

    override fun requestConfirmedByOrganisation(requestSendByUser: RequestSendByUser, currentUserId: String): Boolean {
        return try {
            val user = organisationDao.getOrganisationMember(requestSendByUser.organisationId,currentUserId)
            if(user == null || user.role > 1) return false
            val exist = organisationDao.getOrganisationMember(requestSendByUser.organisationId,requestSendByUser.userId)
            if(exist != null) return false
            val success = organisationDao.addOrganisationMember(
                OrganisationMember(
                    id = "",
                    organisationId = requestSendByUser.organisationId,
                    userId = requestSendByUser.userId,
                    role = 0,
                    joinedAt = System.currentTimeMillis()
                )
            )
            organisationDao.deleteOrganisationJoinRequestSentByUser(requestSendByUser)
            success
        } catch (e: Exception) {
            false
        }
    }

    override fun requestDeclinedByOrganisation(requestSendByUser: RequestSendByUser, currentUserId: String): Boolean {
        return try {
            val user = organisationDao.getOrganisationMember(requestSendByUser.organisationId,currentUserId)
            if(user == null || user.role > 1) return false
            organisationDao.deleteOrganisationJoinRequestSentByUser(requestSendByUser)
        }catch (e: Exception){
            false
        }
    }

    override fun organisationSendRequest(requestSendByOrganisation: RequestSendByOrganisation, currentUserId: String): Boolean {
        return try {
            val user = organisationDao.getOrganisationMember(requestSendByOrganisation.organisationId,currentUserId)
            if(user == null || user.role > 1) return false
            val exist = organisationDao.getOrganisationMember(requestSendByOrganisation.organisationId,requestSendByOrganisation.userId)
            if(exist != null) return false
            organisationDao.addRequestSendByOrganisation(requestSendByOrganisation)
        }catch (e: Exception){
            false
        }
    }

    override fun userSendRequest(requestSendByUser: RequestSendByUser): Boolean {
        return try {
            val exist = organisationDao.getOrganisationMember(requestSendByUser.organisationId,requestSendByUser.userId)
            if(exist != null) return false
            organisationDao.addRequestsSendByUser(requestSendByUser)
            }catch (e: Exception){
            false
        }
    }

    override fun getAllRequestsReceivedByUser(userId: String): List<Organisation> {
        return try {
            organisationDao.getOrganisationJoinRequestsReceivedByUser(userId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getAllRequestSendByUser(userId: String): List<Organisation> {
        return try {
            organisationDao.getOrganisationJoinRequestsSendByUser(userId)
        } catch (e: Exception) {
            emptyList()
        }
    }
}