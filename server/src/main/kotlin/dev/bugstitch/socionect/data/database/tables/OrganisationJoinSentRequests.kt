package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object OrganisationJoinSentRequests : Table("OrganisationJoinSentRequests") {

    val id = varchar("id", 48)
    val organisationId = varchar("organisationId", 48).references(Organisations.id)
    val userId = varchar("userId", 48).references(Users.id)
    val sentAt = long("createdAt") // epoch millis

    override val primaryKey = PrimaryKey(id, name = "PK_OrganisationJoinSendRequests_ID")
}