package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object Users : Table("users") {

    val id = char("id", 24)
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255).uniqueIndex()
    val password = varchar("password", 255)

    override val primaryKey = PrimaryKey(id, name = "PK_Users_ID")
}