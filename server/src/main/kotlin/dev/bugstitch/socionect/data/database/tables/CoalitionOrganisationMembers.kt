package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object CoalitionOrganisationMembers: Table("CoalitionOrganisationMembers") {

    val id = varchar("id", 48)
    val coalitionId = varchar("coalitionId", 48).references(Coalitions.id)
    val userId = varchar("userId", 48).references(Users.id)
    val organisationId = varchar("organisationId", 48).references(Organisations.id)
    val joinedAt = long("joinedAt") // epoch millis
    val isAdmin = bool("isAdmin")
    val canSendMessage = bool("canSendMessage")

    override val primaryKey = PrimaryKey(id, name = "PK_CoalitionOrganisationMembers_ID")

}