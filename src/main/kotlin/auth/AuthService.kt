package com.zhdanon.auth

import com.zhdanon.domain.User
import com.zhdanon.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository = UserRepository()
) {
    fun register(email: String, password: String): Result<Pair<User, Tokens>> {
        if (userRepository.emailExists(email)) {
            return Result.failure(Exception("Email already exists"))
        }

        val hash = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = userRepository.createUser(email, hash)

        val access = JwtConfig.generateAccessToken(user)
        val refresh = JwtConfig.generateRefreshToken(user)

        return Result.success(user to Tokens(access, refresh))
    }

    fun login(email: String, password: String): Result<Pair<User, Tokens>> {
        val user = userRepository.findByEmail(email)
            ?: return Result.failure(Exception("User not found"))

        if (!BCrypt.checkpw(password, user.passwordHash)) {
            return Result.failure(Exception("Invalid password"))
        }

        val access = JwtConfig.generateAccessToken(user)
        val refresh = JwtConfig.generateRefreshToken(user)

        return Result.success(user to Tokens(access, refresh))
    }
}