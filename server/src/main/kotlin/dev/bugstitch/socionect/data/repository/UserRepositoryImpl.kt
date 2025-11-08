package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.UserDao
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.PasswordHasher
import org.bson.types.ObjectId

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override fun createUser(user: User): String {
        return try {
            if (getUserByEmail(user.email) == null && getUserByUsername(user.username) == null) {
                val userId = ObjectId().toHexString()
                val hashedPassword = PasswordHasher.hash(user.password)
                val nUser = user.copy(
                    id = userId,
                    password = hashedPassword
                )
                userDao.createUser(nUser)
            } else {
                "Error: User already exists"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    override fun verifyUser(user: User): Boolean {
        return try {
            val dbUser = getUserByUsername(user.username)
            dbUser != null && PasswordHasher.verify(user.password, dbUser.password)
        } catch (e: Exception) {
            println("Error verifying user: ${e.message}")
            false
        }
    }

    override fun login(user: User): Boolean {
        return try {
            val dbUser = getUserByEmail(user.email)
            dbUser != null && PasswordHasher.verify(user.password, dbUser.password)
        } catch (e: Exception) {
            println("Error during login: ${e.message}")
            false
        }
    }

    override fun updateUser(user: User): String {
        return try {
            userDao.updateUser(user)
        } catch (e: Exception) {
            "Error updating user: ${e.message}"
        }
    }

    override fun deleteUser(user: User): Boolean {
        return try {
            userDao.deleteUser(user)
        } catch (e: Exception) {
            println("Error deleting user: ${e.message}")
            false
        }
    }

    override fun getUser(id: String): User? {
        return try {
            userDao.getUser(id)
        } catch (e: Exception) {
            println("Error fetching user by id: ${e.message}")
            null
        }
    }

    override fun getUserByUsername(username: String): User? {
        return try {
            userDao.getUserByUsername(username)
        } catch (e: Exception) {
            println("Error fetching user by username: ${e.message}")
            null
        }
    }

    override fun getUserByEmail(email: String): User? {
        return try {
            userDao.getUserByEmail(email)
        } catch (e: Exception) {
            println("Error fetching user by email: ${e.message}")
            null
        }
    }

    override fun getAllUsers(): List<User> {
        return try {
            userDao.getAllUsers()
        } catch (e: Exception) {
            println("Error fetching all users: ${e.message}")
            emptyList()
        }
    }
}
