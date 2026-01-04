package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationJoinReceivedRequest
import dev.bugstitch.socionect.domain.models.OrganisationJoinSentRequest
import dev.bugstitch.socionect.domain.models.OrganisationMember

interface OrganisationRepository {

    fun createOrganisation(organisation: Organisation,userId: String): Organisation?

    fun getOrganisationsByMatchingName(name: String): List<Organisation>

    fun getOrganisation(id: String): Organisation?

    fun getUserOrganisations(userId: String): List<Organisation>

    fun addOrganisationMember(organisationMember: OrganisationMember,currentUserId: String): Boolean

    fun getOrganisationMembers(organisationId: String): List<OrganisationMember>

    fun getAllOrganisationJoinSentRequests(organisationId: String): List<OrganisationJoinSentRequest>

    fun getAllOrganisationJoinReceivedRequests(organisationId: String): List<OrganisationJoinReceivedRequest>


    //send by org to user
    fun declineOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest): Boolean

    fun confirmOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest): Boolean

    fun sendOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest,currentUserId: String): Boolean


    //send by user to org
    fun confirmOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest,currentUserId: String): Boolean

    fun declineOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest,currentUserId: String): Boolean

    fun sendOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest): Boolean

    fun getOrganisationJoinSentRequestsForUser(userId: String): List<OrganisationJoinSentRequest>

    fun getOrganisationJoinReceivedRequestsForUser(userId: String): List<OrganisationJoinReceivedRequest>

}
