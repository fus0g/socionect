package dev.bugstitch.socionect.domain.database.repository

import dev.bugstitch.socionect.domain.models.ChatInfo
import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage

interface OrganisationChatDao {

    fun getAllSubTopicChats(subtopicId: String):List<OrganisationSubtopicMessage>

    fun createSubtopicChat(chat: OrganisationSubtopicMessage):Boolean

    fun getAllCoalitionChats(coalitionId: String):List<CoalitionMessage>

    fun createCoalitionChat(coalitionMessage: CoalitionMessage) : Boolean

}