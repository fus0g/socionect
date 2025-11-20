package dev.bugstitch.socionect.domain.database.repository

import dev.bugstitch.socionect.domain.models.User

interface UserDao {

    fun createUser(user: User): String

    fun updateUser(user: User): String

    fun deleteUser(user: User): Boolean

    fun getUser(id: String): User?

    fun getUserByUsername(username: String): User?

    fun getUserByEmail(email: String): User?

    fun getAllUsers(): List<User>

    fun searchUsers(query: String): List<User>

}