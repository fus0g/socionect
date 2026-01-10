package dev.bugstitch.socionect.modules

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.*

object JwtConfig {

    private const val secret = "secret"
    private const val issuer = "http://0.0.0.0:8080/"
    private const val audience = "http://0.0.0.0:8080/"

    val verifier: JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
}

fun Application.authenticationModule() {

    install(Authentication) {
        jwt("auth-jwt-user") {
            realm = "socionect"
            verifier(JwtConfig.verifier)

            validate { credential ->
                if (credential.payload.audience.contains("http://0.0.0.0:8080/")) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}