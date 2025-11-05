package dev.bugstitch.socionect

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

object Users: Table(){
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    val password = varchar("password", 50)
}

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)

fun Application.module() {

    install(ContentNegotiation){
        json()
    }

    val database: Database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/socionect",
        driver = "org.postgresql.Driver",
        user = "user1",
        password = "password"
    )

    transaction(database) {
        SchemaUtils.create(Users)
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        get("/users") {
            var users:List<User> = emptyList()
            transaction(database) {
                users = Users.selectAll().map {
                    User(
                        id = it[Users.id],
                        name = it[Users.name],
                        email = it[Users.email],
                        password = it[Users.password]
                    )
                }
            }
            call.respond(users)
        }
    }
}