package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMember
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage

interface OrganisationSubtopicRepository {

    fun createSubtopic(orgSubtopic: OrganisationSubtopic, userId: String): OrganisationSubtopic?

    fun getAllSubtopics(orgId: String): List<OrganisationSubtopic>

    fun getSubtopic(id: String): OrganisationSubtopic?

    fun deleteSubtopic(id: String, userId: String): Boolean

    fun addSubTopicMember(subtopicMember: OrganisationSubtopicMember, currentUserId: String): Boolean

    fun getSubtopicMembers(subtopicId: String): List<OrganisationSubtopicMember>

    fun getSubtopicMember(subtopicId: String, userId: String): OrganisationSubtopicMember?

    fun removeSubTopicMember(subtopicId: String, userId: String): Boolean

    fun updateSubTopicMember(subtopicMember: OrganisationSubtopicMember,userId: String): Boolean

    fun sendMessage(subtopicId: String, message: String, senderId: String): Boolean

    fun getMessages(subtopicId: String): List<OrganisationSubtopicMessage>

    fun removeAllSubtopicMembers(subtopicId: String,userId: String): Boolean

}