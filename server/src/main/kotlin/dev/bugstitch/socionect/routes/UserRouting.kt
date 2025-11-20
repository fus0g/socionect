package dev.bugstitch.socionect.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.bugstitch.socionect.data.models.TokenDTO
import dev.bugstitch.socionect.data.models.UserDTO
import dev.bugstitch.socionect.data.models.UserSearchRequest
import dev.bugstitch.socionect.data.models.toUser
import dev.bugstitch.socionect.domain.models.toUserDTO
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.PasswordHasher
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.userRouting(userRepository: UserRepository) {

    val secret = "secret"
    val issuer = "http://0.0.0.0:8080/"
    val audience = "http://0.0.0.0:8080/"
    val jwtRealm = "Access to 'hello'"

    routing {

        /**
         * User Signup — Creates a new user and returns access + refresh tokens
         */
        post("/signup") {
            val user = call.receive<UserDTO>()
            val result = userRepository.createUser(user.toUser())

            when {

                result == user.id -> {
                    val accessToken = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("id", user.id)
                        .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .sign(Algorithm.HMAC256(secret))

                    val refreshToken = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("id", user.id)
                        .withExpiresAt(Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000))
                        .sign(Algorithm.HMAC256(secret))

                    call.respond(
                        HttpStatusCode.Created,
                        TokenDTO(
                            token = accessToken,
                            refreshToken = refreshToken
                        )
                    )
                }

                result.contains("already exists", ignoreCase = true) -> {
                    call.respond(HttpStatusCode.Conflict, "Username or email already exists")
                }

                result.startsWith("Error", ignoreCase = true) ||
                        result.contains("Database error", ignoreCase = true) ||
                        result.contains("Unexpected", ignoreCase = true) -> {
                    call.respond(HttpStatusCode.InternalServerError, result)
                }

                else -> {
                    call.respond(HttpStatusCode.BadRequest, "User creation failed: $result")
                }
            }
        }


        /**
         * User Login — Verifies credentials and returns tokens
         */
        post("/login") {
            val loginRequest = call.receive<UserDTO>()

            val user = userRepository.authenticateUser(loginRequest.email, loginRequest.password)

            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }

            val accessToken = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("id", user.id)
                .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret))

            val refreshToken = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("id", user.id)
                .withExpiresAt(Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret))

            call.respond(TokenDTO(token = accessToken, refreshToken = refreshToken))
        }

        /**
         * Refresh Token — Generates new access token from refresh token
         */
        post("/refresh") {
            val refreshRequest = call.receive<Map<String, String>>()
            val refreshToken = refreshRequest["refresh_token"]

            if (refreshToken == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing refresh token")
                return@post
            }

            try {
                val decodedJWT = JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .build()
                    .verify(refreshToken)

                val id = decodedJWT.getClaim("id").asString()

                val newAccessToken = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("id", id)
                    .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 day
                    .sign(Algorithm.HMAC256(secret))

                call.respond(
                    TokenDTO(
                        token = newAccessToken,
                        refreshToken = refreshToken
                    )
                )

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid or expired refresh token")
            }
        }

        /**
         * Get user by email
         */
        post("/user/email") {
            val email = call.receive<UserDTO>()

            val user = userRepository.getUserByEmail(email.email)
            if (user != null) {
                call.respond(user.toUserDTO())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * Get user by username
         */
        post("/user/username") {
            val username = call.receive<UserDTO>()

            val user = userRepository.getUserByUsername(username.username)

            if (user != null) {
                call.respond(user.toUserDTO())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * Protected route
         */
        authenticate("auth-jwt-user") {
            get("/user/hello") {
                val principal = call.principal<JWTPrincipal>()
                val id = principal!!.payload.getClaim("id").asString()
                val user = userRepository.getUser(id)
                if (user != null) {
                    call.respond(user.toUserDTO())
                }
                call.respond(HttpStatusCode.Unauthorized)
            }

            post("/users/search") {
                val req = call.receive<UserSearchRequest>()

                if (req.query.isBlank()) {
                    return@post call.respond(HttpStatusCode.BadRequest, "Query cannot be empty.")
                }

                val results = userRepository.searchUsers(req.query)

                call.respond(results.map { it.toUserDTO() })
            }
        }
    }
}
