package com.zhdanon.auth

import com.zhdanon.models.UserTable
import com.zhdanon.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt

data class AuthRequest(val email: String, val password: String)
data class AuthResponse(val token: String)

fun Route.authRoutes(jwt: JwtService, repo: UserRepository) {

    post("/register") {
        val req = call.receive<AuthRequest>()
        val id = repo.createUser(req.email, req.password)
        call.respond(AuthResponse(jwt.generateToken(id)))
    }

    post("/login") {
        val req = call.receive<AuthRequest>()
        val user = repo.findByEmail(req.email)

        if (user == null || !BCrypt.checkpw(req.password, user[UserTable.password])) {
            call.respondText("Invalid credentials")
            return@post
        }

        call.respond(AuthResponse(jwt.generateToken(user[UserTable.id])))
    }
}