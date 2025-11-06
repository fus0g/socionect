package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.domain.database.repository.UserDao
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.PasswordHasher
import org.bson.types.ObjectId

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override fun createUser(user: User): String {
        if(getUserByEmail(user.email) == null && getUserByUsername(user.username) == null)
        {
            val userId = ObjectId().toHexString()
            val hashedPassword = PasswordHasher.hash(user.password)
            val nUser = user.copy(
                id = userId,
                password = hashedPassword
            )
            return userDao.createUser(nUser)
        }
        return "User already exists"
    }

    override fun verifyUser(user: User): Boolean {
        val dbUser = getUserByUsername(user.username)
        return if(dbUser != null) {
            PasswordHasher.verify(user.password, dbUser.password)
        } else {
            false
        }
    }

    override fun login(user: User): Boolean {
        TODO("Not yet implemented")
    }


    override fun updateUser(user: User): String {
        return userDao.updateUser(user)
    }

    override fun deleteUser(user: User): Boolean {
        return userDao.deleteUser(user)
    }

    override fun getUser(id: String): User? {
        return userDao.getUser(id)
    }

    override fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    override fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    override fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }
}
