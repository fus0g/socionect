package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object OrganisationSubtopicMembers: Table("OrganisationSubtopicMembers") {

    val id = varchar("id", 48)
    val organisationSubtopicId = varchar("organisationSubtopicId", 48).references(OrganisationSubtopics.id)
    val userId = varchar("userId", 48).references(Users.id)
    val joinedAt = long("joinedAt") // epoch millis
    val isAdmin = bool("isAdmin")
    val canSendMessage = bool("canSendMessage")

    override val primaryKey = PrimaryKey(id, name = "PK_OrganisationSubtopicMembers_ID")

}