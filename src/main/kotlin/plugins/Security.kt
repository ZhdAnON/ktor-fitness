package com.zhdanon.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {

    val config = environment.config.config("ktor.security.jwt")

    val secret = config.property("secret").getString()
    val issuer = config.property("issuer").getString()
    val audience = config.property("audience").getString()
    val realm = config.property("realm").getString()

    authentication {
        jwt {
            this.realm = realm

            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
            )

            validate { credential ->
                if (credential.payload.getClaim("id").asInt() != null)
                    JWTPrincipal(credential.payload)
                else
                    null
            }
        }
    }
}