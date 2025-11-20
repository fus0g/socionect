package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.ChatMessage

interface OneToOneChatRepository {

    fun sendMessage(sender: String, receiver: String, message: String): Boolean

    fun getMessages(chatId: String):List<ChatMessage>

}