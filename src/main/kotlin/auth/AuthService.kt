package com.zhdanon.auth

import com.zhdanon.domain.User
import com.zhdanon.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class AuthService(
    val userRepository: UserRepository = UserRepository(),
    private val jwtConfig: JwtConfig
) {
    fun register(email: String, password: String): Result<Pair<User, Tokens>> {
        if (userRepository.emailExists(email)) {
            return Result.failure(Exception("Email already exists"))
        }

        val hash = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = userRepository.createUser(email, hash)

        val access = jwtConfig.generateAccessToken(user)
        val refresh = jwtConfig.generateRefreshToken(user)

        return Result.success(user to Tokens(access, refresh))
    }

    fun login(email: String, password: String): Result<Pair<User, Tokens>> {
        val user = userRepository.findByEmail(email)
            ?: return Result.failure(Exception("User not found"))

        if (!BCrypt.checkpw(password, user.passwordHash)) {
            return Result.failure(Exception("Invalid password"))
        }

        val access = jwtConfig.generateAccessToken(user)
        val refresh = jwtConfig.generateRefreshToken(user)

        return Result.success(user to Tokens(access, refresh))
    }

    suspend fun refresh(refreshToken: String): AuthResponse {
        val decoded = jwtConfig.verifyRefreshToken(refreshToken)
        val userId = UUID.fromString(decoded.getClaim("userId").asString())

        val user = userRepository.findById(userId)
            ?: throw IllegalStateException("User not found")

        val newAccess = jwtConfig.generateAccessToken(user)

        // можно либо оставить старый refresh, либо выдать новый
        val newRefresh = jwtConfig.generateRefreshToken(user)

        return AuthResponse(
            user = UserResponse(
                id = user.id.toString(),
                email = user.email,
                role = user.role
            ),
            accessToken = newAccess,
            refreshToken = newRefresh
        )
    }
}