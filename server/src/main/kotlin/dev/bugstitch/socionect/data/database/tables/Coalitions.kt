package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object Coalitions : Table("Coalitions") {

    val id = varchar("id", 48)
    val name = varchar("name", 255)
    val description = varchar("description", 512)
    val createdAt = long("createdAt") // epoch millis

    override val primaryKey = PrimaryKey(id, name = "PK_Coalition_ID")
}