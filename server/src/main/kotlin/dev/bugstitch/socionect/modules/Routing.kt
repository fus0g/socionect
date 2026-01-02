package dev.bugstitch.socionect.modules

import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.routes.oneToOneChatRouting
import dev.bugstitch.socionect.routes.organisationRouting
import dev.bugstitch.socionect.routes.userRouting
import io.ktor.server.application.Application
import org.koin.ktor.ext.get

fun Application.routing(){

    val userRepository: UserRepository = get()
    val oneToOneChatRepository: OneToOneChatRepository = get()
    val organisationRepository: OrganisationRepository = get()


    userRouting(userRepository)
    oneToOneChatRouting(oneToOneChatRepository)
    organisationRouting(organisationRepository)
}