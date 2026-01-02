package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object OrganisationSubtopicMessages: Table("OrganisationSubtopicMessages") {
    val id = varchar("id", 48)
    val organisationSubtopicId = varchar("organisationSubtopicId", 48).references(OrganisationSubtopics.id)
    val senderId = varchar("senderId", 48).references(Users.id)
    val message = text("message")
    val timestamp = long("timestamp")

    override val primaryKey = PrimaryKey(id, name = "PK_OrganisationSubtopicMessages_ID")
}