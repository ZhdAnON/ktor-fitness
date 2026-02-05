package com.zhdanon.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*

class JwtService(config: ApplicationConfig) {

    private val secret = config.property("ktor.security.jwt.secret").getString()
    private val issuer = config.property("ktor.security.jwt.issuer").getString()
    private val audience = config.property("ktor.security.jwt.audience").getString()
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: Int): String =
        JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("id", userId)
            .sign(algorithm)
}