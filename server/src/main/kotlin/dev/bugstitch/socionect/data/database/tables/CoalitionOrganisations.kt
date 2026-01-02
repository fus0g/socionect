package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object CoalitionOrganisations: Table("CoalitionOrganisations") {

    val id = varchar("id", 48)
    val coalitionId = varchar("coalitionId", 48).references(Coalitions.id)
    val organisationId = varchar("organisationId", 48).references(Organisations.id)
    val joinedAt = long("joinedAt") // epoch millis

    override val primaryKey = PrimaryKey(id, name = "PK_CoalitionOrganisations_ID")

}