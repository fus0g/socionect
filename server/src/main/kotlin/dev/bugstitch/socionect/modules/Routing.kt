package dev.bugstitch.socionect.modules

import dev.bugstitch.socionect.domain.repository.CoalitionRepository
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.domain.repository.OrganisationChatRepository
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.routes.coalitionRouting
import dev.bugstitch.socionect.routes.oneToOneChatRouting
import dev.bugstitch.socionect.routes.organisationChatRouting
import dev.bugstitch.socionect.routes.organisationRouting
import dev.bugstitch.socionect.routes.organisationSubtopicRouting
import dev.bugstitch.socionect.routes.userRouting
import io.ktor.server.application.Application
import org.koin.ktor.ext.get

fun Application.routing(){

    val userRepository: UserRepository = get()
    val oneToOneChatRepository: OneToOneChatRepository = get()
    val organisationRepository: OrganisationRepository = get()
    val subtopicRepository: OrganisationSubtopicRepository = get()
    val coalitionRepository: CoalitionRepository = get()
    val organisationChatRepository: OrganisationChatRepository = get()

    userRouting(userRepository)
    oneToOneChatRouting(oneToOneChatRepository)
    organisationRouting(organisationRepository)
    organisationSubtopicRouting(subtopicRepository)
    coalitionRouting(coalitionRepository)
    organisationChatRouting(organisationChatRepository)
}