package dev.bugstitch.socionect.modules

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.*

fun Application.authenticationModule(){

    val secret = "secret"
    val issuer = "http://0.0.0.0:8080/"
    val audience = "http://0.0.0.0:8080/"
    val jwtRealm = "Access to 'hello'"

    install(Authentication){
        jwt {
            realm = jwtRealm
            verifier {
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            }

            validate { cred->
                if(cred.payload.audience.contains(audience))
                {
                    JWTPrincipal(cred.payload)
                }else{
                    null
                }
            }
        }
    }
}