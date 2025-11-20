package dev.bugstitch.socionect.domain.database.repository

import dev.bugstitch.socionect.domain.models.ChatInfo
import dev.bugstitch.socionect.domain.models.ChatMessage


interface OneToOneChatDao {

    fun createChat(chatId: String): ChatInfo?

    fun getChat(chatId: String): ChatInfo?

    fun addMessage(chatId: String,senderId: String, message: String): Boolean

    fun getAllMessages(chatId: String): List<ChatMessage>

}