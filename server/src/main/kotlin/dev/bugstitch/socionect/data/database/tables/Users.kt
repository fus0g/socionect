package dev.bugstitch.socionect.data.database.tables

import org.jetbrains.exposed.v1.core.Table

object Users: Table() {

    val id = char(name = "id", length = 24)
    val name = varchar(name = "name", length = 255)
    val email = varchar(name = "email", length = 255)
    val username = varchar(name = "username", length = 255)
    val password = varchar(name = "password", length = 255)

}