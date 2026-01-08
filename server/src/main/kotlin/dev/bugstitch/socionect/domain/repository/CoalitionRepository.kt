package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation

interface CoalitionRepository{

    fun createCoalition(coalition: Coalition, hostOrg:Organisation, userId: String, list: List<Organisation>) : Boolean

    fun getAllOrganisationCoalition(id: String,userId: String): List<Coalition>

    fun getAllOrganisationsInCoalition(id: String): List<Organisation>

    fun getCoalition(id: String): Coalition?

    fun createCoalitionRequest(request: CoalitionRequest,userId: String) : Boolean

    fun acceptCoalitionRequest(request: CoalitionRequest,userId: String) : Boolean

    fun declineCoalitionRequest(request: CoalitionRequest,userId: String) : Boolean

    fun getAllCoalitionRequests(orgId: String): List<CoalitionRequest>
}