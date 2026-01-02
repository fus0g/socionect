package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object OrganisationJoinReceivedRequests: Table("OrganisationJoinReceivedRequests") {

    val id = varchar("id", 48)
    val organisationId = varchar("organisationId", 48).references(Organisations.id)
    val userId = varchar("userId", 48).references(Users.id)
    val receivedAt = long("createdAt") // epoch millis

    override val primaryKey = PrimaryKey(id, name = "PK_OrganisationJoinReceivedRequests_ID")
}