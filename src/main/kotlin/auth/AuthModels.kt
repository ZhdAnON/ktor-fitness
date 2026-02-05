package com.zhdanon.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String
)

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String
)