package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.CoalitionMessage
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage

interface OrganisationChatRepository {

    fun getAllSubTopicChats(userId: String, subtopicId: String):List<OrganisationSubtopicMessage>

    fun createSubTopicMessage(userId: String,organisationSubtopicMessage: OrganisationSubtopicMessage):Boolean

    fun getAllCoalitionChats(userId: String,coalitionId: String):List<CoalitionMessage>

    fun createCoalitionMessage(userId: String,message: CoalitionMessage):Boolean

}