package dev.bugstitch.socionect.utils

import de.mkammerer.argon2.Argon2Factory

object PasswordHasher {

    private val argon2 = Argon2Factory.create()

    fun hash(password: String): String =
        argon2.hash(4, 1024 * 1024, 8, password.toCharArray())

    fun verify(password: String, hash: String): Boolean =
        argon2.verify(hash, password.toCharArray())

}