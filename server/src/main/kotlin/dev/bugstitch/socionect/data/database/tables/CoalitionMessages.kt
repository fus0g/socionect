package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object CoalitionMessages: Table("CoalitionMessages") {
    val id = varchar("id", 48)
    val coalitionId = varchar("coalitionId", 48).references(Coalitions.id)
    val senderId = varchar("senderId", 48).references(Users.id)
    val message = text("message")
    val timestamp = long("timestamp")

    override val primaryKey = PrimaryKey(id, name = "PK_CoalitionMessages_ID")
}