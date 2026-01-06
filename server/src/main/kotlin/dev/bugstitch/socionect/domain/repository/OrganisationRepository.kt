package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.domain.models.RequestSendByUser
import dev.bugstitch.socionect.domain.models.RequestSendByOrganisation
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.models.User

interface OrganisationRepository {

    fun createOrganisation(organisation: Organisation,userId: String): Organisation?

    fun getOrganisationsByMatchingName(name: String): List<Organisation>

    fun getOrganisation(id: String): Organisation?

    fun getUserOrganisations(userId: String): List<Organisation>

    fun addOrganisationMember(organisationMember: OrganisationMember,currentUserId: String): Boolean

    fun getOrganisationMembers(organisationId: String): List<User>

    fun getAllRequestsSendByOrganisation(organisationId: String): List<User>

    fun getAllRequestsReceivedByOrganisation(organisationId: String): List<User>


    //send by org to user
    fun requestDeclinedByUser(requestSendByOrganisation: RequestSendByOrganisation): Boolean

    fun requestConfirmedByUser(requestSendByOrganisation: RequestSendByOrganisation): Boolean

    fun organisationSendRequest(requestSendByOrganisation: RequestSendByOrganisation, currentUserId: String): Boolean


    //send by user to org
    fun requestConfirmedByOrganisation(requestSendByUser: RequestSendByUser, currentUserId: String): Boolean

    fun requestDeclinedByOrganisation(requestSendByUser: RequestSendByUser, currentUserId: String): Boolean

    fun userSendRequest(requestSendByUser: RequestSendByUser): Boolean

    fun getAllRequestsReceivedByUser(userId: String): List<Organisation>

    fun getAllRequestSendByUser(userId: String): List<Organisation>

}
