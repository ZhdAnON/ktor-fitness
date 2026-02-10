package com.zhdanon.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.zhdanon.domain.User
import java.util.*

object JwtConfig {
    private const val issuer = "ktor-fitness"
    private const val accessValidityMs = 1000L * 60L * 15L // 15 минут
    private const val refreshValidityMs = 1000L * 60L * 60L * 24L * 30L // 30 дней

    private val secret = System.getenv("JWT_SECRET") ?: "dev-secret"
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(user: User): String =
        JWT.create()
            .withIssuer(issuer)
            .withClaim("userId", user.id.toString())
            .withClaim("email", user.email)
            .withClaim("role", user.role)
            .withExpiresAt(Date(System.currentTimeMillis() + accessValidityMs))
            .sign(algorithm)

    fun generateRefreshToken(user: User): String =
        JWT.create()
            .withIssuer(issuer)
            .withClaim("userId", user.id.toString())
            .withClaim("type", "refresh")
            .withExpiresAt(Date(System.currentTimeMillis() + refreshValidityMs))
            .sign(algorithm)

    fun verifier() =
        JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
}