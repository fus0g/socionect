package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object CoalitionRequests : Table("CoalitionRequests") {

    val id = varchar("id", 48)
    val coalitionId = varchar("coalitionId", 48).references(Coalitions.id)
    val organisationId = varchar("organisationId", 48).references(Organisations.id)
    val sentAt = long("createdAt") // epoch millis

    override val primaryKey = PrimaryKey(id, name = "PK_CoalitionRequests_ID")

}