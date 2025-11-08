package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

fun UserDTO.toUser(): User = User(
    id = this.id,
    name = this.name,
    username = this.username,
    email = this.email,
    password = this.password
)
