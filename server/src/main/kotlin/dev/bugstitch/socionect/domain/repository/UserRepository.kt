package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.domain.models.User

interface UserRepository {
    fun createUser(user: User): String
    fun updateUser(user: User): String
    fun deleteUser(user: User): Boolean
    fun getUser(id: String): User?
    fun getUserByUsername(username: String): User?
    fun getUserByEmail(email: String): User?
    fun getAllUsers(): List<User>

    fun verifyUser(user: User): Boolean

    fun login(user: User): Boolean

    fun authenticateUser(email: String, password: String): User?

    fun searchUsers(query: String): List<User>
}
