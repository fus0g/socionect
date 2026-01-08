package dev.bugstitch.socionect.domain.database.repository

import dev.bugstitch.socionect.domain.models.Coalition
import dev.bugstitch.socionect.domain.models.CoalitionOrganisation
import dev.bugstitch.socionect.domain.models.CoalitionRequest
import dev.bugstitch.socionect.domain.models.Organisation

interface CoalitionDao {

    fun addCoalition(coalition: Coalition) : Coalition?

    fun getCoalition(id: String): Coalition?

    fun addOrganisationToCoalition(coalitionOrganisation: CoalitionOrganisation) : CoalitionOrganisation?

    fun getAllCoalitionOrganizations(organisationId: String): List<Organisation>

    fun getAllOrganisationCoalitions(id: String): List<Coalition>

    fun createCoalitionRequest(coalitionRequest: CoalitionRequest) : CoalitionRequest?

    fun getAllCoalitionRequests(organisationId: String): List<CoalitionRequest>

    fun deleteCoalitionRequest(coalitionRequest: CoalitionRequest) : Boolean

}