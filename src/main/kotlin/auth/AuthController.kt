package com.zhdanon.auth

import com.zhdanon.models.UserTable
import com.zhdanon.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.ResultRow
import org.mindrot.jbcrypt.BCrypt

fun Route.authRoutes(jwt: JwtService, repo: UserRepository) {

    post("/register") {
        val req = runCatching { call.receive<AuthRequest>() }.getOrElse {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("BAD_REQUEST", "Некорректное тело запроса")
            )
            return@post
        }

        val emailError = validateEmail(req.email)
        val passwordError = validatePassword(req.password)

        if (emailError != null || passwordError != null) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    code = "VALIDATION_ERROR",
                    message = emailError ?: passwordError!!
                )
            )
            return@post
        }

        val existing = repo.findByEmail(req.email)
        if (existing != null) {
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse("USER_EXISTS", "Пользователь с таким email уже существует")
            )
            return@post
        }

        val hash = BCrypt.hashpw(req.password, BCrypt.gensalt())
        val id = repo.createUser(req.email, hash)

        val token = jwt.generateToken(id)
        call.respond(HttpStatusCode.Created, AuthResponse(token))
    }

    post("/login") {
        val req = runCatching { call.receive<AuthRequest>() }.getOrElse {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("BAD_REQUEST", "Некорректное тело запроса")
            )
            return@post
        }

        val user: ResultRow? = repo.findByEmail(req.email)
        if (user == null || !BCrypt.checkpw(req.password, user[UserTable.password])) {
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("INVALID_CREDENTIALS", "Неверный email или пароль")
            )
            return@post
        }

        val token = jwt.generateToken(user[UserTable.id])
        call.respond(AuthResponse(token))
    }
}