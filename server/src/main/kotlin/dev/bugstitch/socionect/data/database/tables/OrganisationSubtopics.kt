package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object OrganisationSubtopics: Table("OrganisationSubtopics") {

    val id = varchar("id", 48)
    val organisationId = varchar("organisationId", 48).references(Organisations.id)
    val name = varchar("name", 255)
    val description = varchar("description", 512)
    val createdAt = long("createdAt") // epoch millis

    override val primaryKey = PrimaryKey(id, name = "PK_OrganisationSubtopics_ID")
}