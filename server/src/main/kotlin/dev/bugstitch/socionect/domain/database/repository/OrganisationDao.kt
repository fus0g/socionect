package dev.bugstitch.socionect.domain.database.repository

import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.RequestSendByUser
import dev.bugstitch.socionect.domain.models.RequestSendByOrganisation
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.models.User

interface OrganisationDao {

    fun createOrganisation(organisation: Organisation): Organisation?

    fun getAllOrganisations(): List<Organisation>

    fun getAllUserOrganisation(userId: String): List<Organisation>

    fun getOrganisationByMatchingName(name: String): List<Organisation>

    fun getOrganisation(id: String): Organisation?

    fun getOrganisationMember(organisationId: String,memberId: String): OrganisationMember?

    fun addOrganisationMember(organisationMember: OrganisationMember): Boolean

    fun getOrganisationMembers(organisationId: String): List<OrganisationMember>

    fun addRequestSendByOrganisation(requestSendByOrganisation: RequestSendByOrganisation) : Boolean

    fun addRequestsSendByUser(requestSendByUser: RequestSendByUser): Boolean

    fun getOrganisationJoinRequestSendByOrganisation(organisationId: String): List<RequestSendByOrganisation>

    fun getOrganisationJoinRequestReceivedByOrganisation(organisationId: String): List<User>

    fun getOrganisationJoinRequestsSendByUser(userId: String): List<Organisation>

    fun getOrganisationJoinRequestsReceivedByUser(userId: String): List<RequestSendByOrganisation>

    fun deleteOrganisationJoinRequestSendByOrganisation(requestSendByOrganisation: RequestSendByOrganisation): Boolean

    fun deleteOrganisationJoinRequestSentByUser(requestSendByUser: RequestSendByUser): Boolean

}