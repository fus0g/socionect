package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object OneToOneChats : Table("OneToOneChats") {
    val chatId = varchar("chatId", 64)
    val userId1 = varchar("userId1", 48).references(Users.id)
    val userId2 = varchar("userId2", 48).references(Users.id)
    val createdAt = long("createdAt") // epoch millis

    override val primaryKey = PrimaryKey(chatId, name = "PK_OneToOneChats_ChatId")
}