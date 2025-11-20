package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.OneToOneChatDao
import dev.bugstitch.socionect.domain.database.repository.UserDao
import dev.bugstitch.socionect.domain.models.ChatMessage
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository

class OneToOneChatRepositoryImpl(
    private val chatDao: OneToOneChatDao,
    private val userDao: UserDao
): OneToOneChatRepository {

    override fun sendMessage(
        sender: String,
        receiver: String,
        message: String
    ): Boolean {
        try {
            val user1 = userDao.getUser(sender)
            val user2 = userDao.getUser(receiver)

            if (user1 == null || user2 == null) return false

            val chatId = listOf(sender,receiver).sorted().joinToString("")

            val chat = chatDao.getChat(chatId)
            if(chat == null)
            {
                chatDao.createChat(chatId) ?: return false
            }
            val send = chatDao.addMessage(chatId,sender,message)

            return send
        }catch (e: Exception)
        {
            return false
        }

    }

    override fun getMessages(chatId: String): List<ChatMessage> {
        return chatDao.getAllMessages(chatId)
    }
}