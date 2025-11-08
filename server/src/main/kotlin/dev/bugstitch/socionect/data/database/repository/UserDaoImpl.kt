package dev.bugstitch.socionect.data.database.repository

import dev.bugstitch.socionect.data.database.tables.Users
import dev.bugstitch.socionect.domain.database.repository.UserDao
import dev.bugstitch.socionect.domain.models.User
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.sql.SQLException

class UserDaoImpl(private val database: Database) : UserDao {

    override fun createUser(user: User): String {
        return try {
            transaction(database) {
                Users.insert {
                    it[id] = user.id
                    it[name] = user.name
                    it[username] = user.username
                    it[email] = user.email
                    it[password] = user.password
                }
            }
            user.username
        } catch (e: SQLException) {
            when {
                e.message?.contains("UNIQUE constraint", ignoreCase = true) == true ||
                        e.message?.contains("duplicate key", ignoreCase = true) == true -> {
                    "Error: Username or email already exists"
                }
                else -> "Database error: ${e.message}"
            }
        } catch (e: Exception) {
            println("Unexpected error while creating user: ${e.message}")
            "Unexpected error: ${e.message}"
        }
    }

    override fun updateUser(user: User): String {
        return try {
            val updated = transaction(database) {
                Users.update({ Users.id eq user.id }) {
                    it[name] = user.name
                    it[username] = user.username
                    it[email] = user.email
                    it[password] = user.password
                }
            }

            if (updated > 0) user.id else "Error: User not found"
        } catch (e: SQLException) {
            "Database error while updating: ${e.message}"
        } catch (e: Exception) {
            println("Unexpected error updating user: ${e.message}")
            "Unexpected error: ${e.message}"
        }
    }

    override fun deleteUser(user: User): Boolean {
        return try {
            transaction(database) {
                Users.deleteWhere { Users.id eq user.id } > 0
            }
        } catch (e: SQLException) {
            println("Database error while deleting user: ${e.message}")
            false
        } catch (e: Exception) {
            println("Unexpected error deleting user: ${e.message}")
            false
        }
    }

    override fun getUser(id: String): User? {
        return try {
            transaction(database) {
                Users.selectAll().where { Users.id eq id }.map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        username = it[Users.username],
                        email = it[Users.email],
                        password = it[Users.password]
                    )
                }.singleOrNull()
            }
        } catch (e: SQLException) {
            println("Database error fetching user by id: ${e.message}")
            null
        } catch (e: Exception) {
            println("Unexpected error fetching user by id: ${e.message}")
            null
        }
    }

    override fun getUserByUsername(username: String): User? {
        return try {
            transaction(database) {
                Users.selectAll().where { Users.username eq username }.map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        username = it[Users.username],
                        email = it[Users.email],
                        password = it[Users.password]
                    )
                }.singleOrNull()
            }
        } catch (e: SQLException) {
            println("Database error fetching user by username: ${e.message}")
            null
        } catch (e: Exception) {
            println("Unexpected error fetching user by username: ${e.message}")
            null
        }
    }

    override fun getUserByEmail(email: String): User? {
        return try {
            transaction(database) {
                Users.selectAll().where { Users.email eq email }.map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        username = it[Users.username],
                        email = it[Users.email],
                        password = it[Users.password]
                    )
                }.singleOrNull()
            }
        } catch (e: SQLException) {
            println("Database error fetching user by email: ${e.message}")
            null
        } catch (e: Exception) {
            println("Unexpected error fetching user by email: ${e.message}")
            null
        }
    }

    override fun getAllUsers(): List<User> {
        return try {
            transaction(database) {
                Users.selectAll().map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        username = it[Users.username],
                        email = it[Users.email],
                        password = it[Users.password]
                    )
                }
            }
        } catch (e: SQLException) {
            println("Database error fetching all users: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            println("Unexpected error fetching all users: ${e.message}")
            emptyList()
        }
    }
}
