package com.zhdanon.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val role: String)

@Serializable
data class AuthResponse(
    val user: UserResponse,
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class RefreshRequest(
    val refreshToken: String
)

data class Tokens(val access: String, val refresh: String)