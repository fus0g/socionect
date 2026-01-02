package dev.bugstitch.socionect.domain.database.repository

import dev.bugstitch.socionect.domain.models.OrganisationSubtopic
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMember
import dev.bugstitch.socionect.domain.models.OrganisationSubtopicMessage

interface OrganisationSubtopicDao {

    fun createSubtopic(orgSubtopic: OrganisationSubtopic): OrganisationSubtopic?

    fun getAllSubtopics(orgId: String): List<OrganisationSubtopic>

    fun getSubtopic(id: String): OrganisationSubtopic?

    fun deleteSubtopic(id: String): Boolean

    fun addSubTopicMember(subtopicMember: OrganisationSubtopicMember): Boolean

    fun getSubtopicMembers(subtopicId: String): List<OrganisationSubtopicMember>

    fun getSubtopicMember(subtopicId: String,userId: String): OrganisationSubtopicMember?

    fun removeSubTopicMember(subtopicId: String,userId: String): Boolean

    fun updateSubTopicMember(subtopicMember: OrganisationSubtopicMember): Boolean

    fun addMessage(subtopicId: String,message: String,senderId: String): Boolean

    fun getMessages(subtopicId: String): List<OrganisationSubtopicMessage>

    fun removeAllSubtopicMembers(subtopicId: String): Boolean

}