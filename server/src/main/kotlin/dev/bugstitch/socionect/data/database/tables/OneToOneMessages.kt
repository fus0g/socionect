package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object OneToOneMessages : Table("oneToOneMessages") {
    val messageId = long("messageId").autoIncrement()
    val chatId = varchar("chatId", 64).references(OneToOneChats.chatId)
    val senderId = varchar("senderId", 24).references(Users.id)
    val message = text("message")
    val timestamp = long("timestamp")


    override val primaryKey = PrimaryKey(messageId, name = "PK_OneToOneMessages_MessageId")
}