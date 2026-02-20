package com.zhdanon.auth

import com.zhdanon.models.domain.User
import com.zhdanon.repository.UserRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import org.mindrot.jbcrypt.BCrypt

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
        val (refresh, refreshExpires) = jwtConfig.generateRefreshToken(user)

        val expiresAt = Instant.fromEpochMilliseconds(refreshExpires.time)
            .toLocalDateTime(TimeZone.UTC)

        userRepository.updateRefreshToken(user.id, refresh, expiresAt)

        return Result.success(user to Tokens(access, refresh))
    }

    fun login(email: String, password: String): Result<Pair<User, Tokens>> {
        val user = userRepository.findByEmail(email)
            ?: return Result.failure(Exception("User not found"))

        if (!BCrypt.checkpw(password, user.passwordHash)) {
            return Result.failure(Exception("Invalid password"))
        }

        val access = jwtConfig.generateAccessToken(user)
        val (refresh, refreshExpires) = jwtConfig.generateRefreshToken(user)

        val expiresAt = Instant.fromEpochMilliseconds(refreshExpires.time)
            .toLocalDateTime(TimeZone.UTC)

        userRepository.updateRefreshToken(user.id, refresh, expiresAt)

        return Result.success(user to Tokens(access, refresh))
    }

    suspend fun refresh(refreshToken: String): AuthResponse {
        // 1. Проверяем подпись
        jwtConfig.verifyRefreshToken(refreshToken)

        // 2. Проверяем refresh-токен в базе
        val user = userRepository.findByRefreshToken(refreshToken)
            ?: throw IllegalStateException("Invalid refresh token")

        // 3. Проверяем срок действия
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        if (user.refreshExpiresAt == null || user.refreshExpiresAt < now) {
            throw IllegalStateException("Refresh token expired")
        }

        // 4. Генерируем новые токены
        val newAccess = jwtConfig.generateAccessToken(user)
        val (newRefresh, newRefreshExpires) = jwtConfig.generateRefreshToken(user)

        val newExpiresAt = Instant.fromEpochMilliseconds(newRefreshExpires.time)
            .toLocalDateTime(TimeZone.UTC)

        // 5. Обновляем refresh-токен в базе
        userRepository.updateRefreshToken(user.id, newRefresh, newExpiresAt)

        // 6. Возвращаем клиенту
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