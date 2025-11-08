package dev.bugstitch.socionect.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.bugstitch.socionect.data.models.TokenDTO
import dev.bugstitch.socionect.data.models.UserDTO
import dev.bugstitch.socionect.data.models.toUser
import dev.bugstitch.socionect.domain.models.toUserDTO
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.PasswordHasher
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.util.Date

fun Application.userRouting(userRepository: UserRepository){

    val secret = "secret"
    val issuer = "http://0.0.0.0:8080/"
    val audience = "http://0.0.0.0:8080/"
    val jwtRealm = "Access to 'hello'"

    routing {

        post("/signup"){
            val user = call.receive<UserDTO>()
            val result = userRepository.createUser(user.toUser())

            if(result == user.username)
            {
                call.respond(HttpStatusCode.Created)
            }else{
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/login"){
            val user  = call.receive<UserDTO>()
            val validUser = userRepository.getUserByEmail(user.email)?.let {
                PasswordHasher.verify(user.password,it.password)
            } ?:false

            if(!validUser)
            {
                call.respond(HttpStatusCode.Unauthorized,"Invalid Credentials")
                return@post
            }

            val accessToken = JWT.create()
                .withAudience(secret)
                .withIssuer(issuer)
                .withAudience(audience)
                .withClaim("username",user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret))

            val refreshToken = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret))

            call.respond(
                TokenDTO(
                    accessToken,
                    refreshToken
                )
            )

        }

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

                val username = decodedJWT.getClaim("username").asString()

                val newAccessToken = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 day
                    .sign(Algorithm.HMAC256(secret))

                call.respond(TokenDTO(
                    token = newAccessToken,
                    refreshToken = refreshToken
                ))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid or expired refresh token")
            }
        }

        post("/user/email"){
            val email = call.receive<UserDTO>()

            val user = userRepository.getUserByEmail(email.email)
            if(user != null)
            {
                call.respond(user.toUserDTO())
            }else{
                call.respond(HttpStatusCode.NotFound)
            }
        }

        post("/user/username"){
            val username = call.receive<UserDTO>()

            val user = userRepository.getUserByUsername(username.username)

            if(user != null)
            {
                call.respond(user.toUserDTO())
            }
            else{
                call.respond(HttpStatusCode.NotFound)
            }
        }

        authenticate("auth-jwt-user"){

            get("/user/hello"){
                call.respond(HttpStatusCode.OK)
            }

        }

    }
}