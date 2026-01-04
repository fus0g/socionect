package dev.bugstitch.socionect.domain.database.repository

import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.OrganisationJoinReceivedRequest
import dev.bugstitch.socionect.domain.models.OrganisationJoinSentRequest
import dev.bugstitch.socionect.domain.models.OrganisationMember

interface OrganisationDao {

    fun createOrganisation(organisation: Organisation): Organisation?

    fun getAllOrganisations(): List<Organisation>

    fun getAllUserOrganisation(userId: String): List<Organisation>

    fun getOrganisationByMatchingName(name: String): List<Organisation>

    fun getOrganisation(id: String): Organisation?

    fun getOrganisationMember(organisationId: String,memberId: String): OrganisationMember?

    fun addOrganisationMember(organisationMember: OrganisationMember): Boolean

    fun getOrganisationMembers(organisationId: String): List<OrganisationMember>

    fun addOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest) : Boolean

    fun addOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest): Boolean

    fun getOrganisationJoinSentRequests(organisationId: String): List<OrganisationJoinSentRequest>

    fun getOrganisationJoinReceivedRequests(organisationId: String): List<OrganisationJoinReceivedRequest>

    fun getOrganisationJoinReceivedRequestsForUser(userId: String): List<OrganisationJoinReceivedRequest>

    fun getOrganisationJoinSentRequestsForUser(userId: String): List<OrganisationJoinSentRequest>

    fun deleteOrganisationJoinSentRequest(organisationJoinSentRequest: OrganisationJoinSentRequest): Boolean

    fun deleteOrganisationJoinReceivedRequest(organisationJoinReceivedRequest: OrganisationJoinReceivedRequest): Boolean

}