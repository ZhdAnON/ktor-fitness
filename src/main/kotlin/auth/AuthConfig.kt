package com.zhdanon.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuth() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.verifier())
            validate { credential ->
                val email = credential.payload.getClaim("email").asString()
                if (email != null) JWTPrincipal(credential.payload) else null
            }
        }
    }
}