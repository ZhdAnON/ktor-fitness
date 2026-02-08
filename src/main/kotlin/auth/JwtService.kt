package com.zhdanon.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.*

class JwtService(config: ApplicationConfig) {

    private val secret = config.property("ktor.security.jwt.secret").getString()
    private val issuer = config.property("ktor.security.jwt.issuer").getString()
    val audience = config.property("ktor.security.jwt.audience").getString()

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun generateToken(userId: Int): String =
        JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 7L)) // 7 дней
            .sign(algorithm)
}