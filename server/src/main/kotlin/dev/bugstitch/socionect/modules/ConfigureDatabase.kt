package dev.bugstitch.socionect.modules

import dev.bugstitch.socionect.data.database.tables.Users
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureDatabase(database: Database){

    transaction(database) {
        SchemaUtils.create(Users)
    }

}