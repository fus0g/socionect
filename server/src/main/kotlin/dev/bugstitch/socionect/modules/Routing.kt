package dev.bugstitch.socionect.modules

import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.routes.userRouting
import io.ktor.server.application.Application
import org.koin.ktor.ext.get

fun Application.routing(){

    val userRepository: UserRepository = get()
    userRouting(userRepository)
}