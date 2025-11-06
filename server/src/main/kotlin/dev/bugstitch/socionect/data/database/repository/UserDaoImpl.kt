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

class UserDaoImpl(private val database: Database) : UserDao {

    override fun createUser(user: User): String {
        transaction(database) {
            Users.insert {
                it[id] = user.id
                it[name] = user.name
                it[username] = user.username
                it[email] = user.email
                it[password] = user.password
            }
        }
        return user.username
    }

    override fun updateUser(user: User): String {
        transaction(database) {
            Users.update({ Users.id eq user.id }) {
                it[name] = user.name
                it[username] = user.username
                it[email] = user.email
                it[password] = user.password
            }
        }
        return user.id
    }

    override fun deleteUser(user: User): Boolean {
        return transaction(database) {
            Users.deleteWhere { Users.id eq user.id } > 0
        }
    }

    override fun getUser(id: String): User? {
        return transaction(database) {
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
    }

    override fun getUserByUsername(username: String): User? {
        return transaction(database) {
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
    }

    override fun getUserByEmail(email: String): User? {
        return transaction(database) {
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
    }

    override fun getAllUsers(): List<User> {
        return transaction(database) {
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
    }
}
