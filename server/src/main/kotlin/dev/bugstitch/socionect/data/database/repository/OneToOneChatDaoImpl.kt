package dev.bugstitch.socionect.data.database.repository

import dev.bugstitch.socionect.data.database.tables.OneToOneChats
import dev.bugstitch.socionect.data.database.tables.OneToOneMessages
import dev.bugstitch.socionect.data.database.tables.Users
import dev.bugstitch.socionect.domain.database.repository.OneToOneChatDao
import dev.bugstitch.socionect.domain.models.ChatInfo
import dev.bugstitch.socionect.domain.models.ChatMessage
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class OneToOneChatDaoImpl(private val database: Database) : OneToOneChatDao {

    override fun createChat(chatId: String): ChatInfo? {
        return transaction(database) {
            try {
                if (chatId.length != 48) return@transaction null

                val userId1 = chatId.substring(0, 24)
                val userId2 = chatId.substring(24, 48)

                val exists1 = Users.select(Users.id eq userId1).count() > 0
                val exists2 = Users.select(Users.id eq userId2).count() > 0
                if (!exists1 || !exists2) return@transaction null

                getChat(chatId)?.let { return@transaction it }

                val createdAt = System.currentTimeMillis()

                try {
                    transaction {
                        OneToOneChats.insert { row ->
                            row[OneToOneChats.chatId] = chatId
                            row[OneToOneChats.userId1] = userId1
                            row[OneToOneChats.userId2] = userId2
                            row[OneToOneChats.createdAt] = createdAt
                        }
                    }

                    ChatInfo(chatId, userId1, userId2, createdAt)

                } catch (e: ExposedSQLException) {
                    if (e.message?.contains("duplicate key") == true) {
                        return@transaction getChat(chatId)
                    } else {
                        e.printStackTrace()
                        null
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    override fun getChat(chatId: String): ChatInfo? {
        return transaction(database) {
            try {
                val chat = OneToOneChats
                    .selectAll()
                    .where { OneToOneChats.chatId eq chatId }
                    .singleOrNull()

                if(chat == null)
                {
                    return@transaction null
                }

                return@transaction ChatInfo(
                    chatId = chat[OneToOneChats.chatId],
                    userId1 = chat[OneToOneChats.userId1],
                    userId2 = chat[OneToOneChats.userId2],
                    createdAt = chat[OneToOneChats.createdAt]
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun addMessage(chatId: String, senderId: String, message: String): Boolean {
        return transaction(database) {
            try {
                val chat = OneToOneChats
                    .selectAll()
                    .where { OneToOneChats.chatId eq chatId }
                    .singleOrNull()
                    ?: return@transaction false

                val user1 = chat[OneToOneChats.userId1]
                val user2 = chat[OneToOneChats.userId2]

                if (senderId != user1 && senderId != user2) {
                    return@transaction false
                }

                OneToOneMessages.insert { row ->
                    row[OneToOneMessages.chatId] = chatId
                    row[OneToOneMessages.senderId] = senderId
                    row[OneToOneMessages.message] = message
                    row[OneToOneMessages.timestamp] = System.currentTimeMillis()
                }

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override fun getAllMessages(chatId: String): List<ChatMessage> {
        return transaction(database) {
            try {
                OneToOneMessages
                    .selectAll()
                    .where{OneToOneMessages.chatId eq chatId}
                    .orderBy(OneToOneMessages.messageId to SortOrder.ASC)
                    .map { row ->
                        ChatMessage(
                            messageId = row[OneToOneMessages.messageId],
                            chatId = row[OneToOneMessages.chatId],
                            senderId = row[OneToOneMessages.senderId],
                            message = row[OneToOneMessages.message],
                            timestamp = row[OneToOneMessages.timestamp]
                        )
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}