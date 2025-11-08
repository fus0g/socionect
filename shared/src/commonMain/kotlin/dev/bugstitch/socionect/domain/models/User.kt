package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.UserDTO

data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

fun User.toUserDTO(): UserDTO = UserDTO(
    id = this.id,
    name = this.name,
    username = this.username,
    email = this.email,
    password = this.password
)
