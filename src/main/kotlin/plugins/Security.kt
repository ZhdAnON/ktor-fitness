package com.zhdanon.plugins

import com.zhdanon.auth.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val jwt = JwtService(environment.config)

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(jwt.verifier)
            validate { credential ->
                val audience = credential.payload.audience
                if (audience.contains(jwt.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }
}