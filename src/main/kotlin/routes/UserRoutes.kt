package com.zhdanon.routes

import com.zhdanon.auth.UserResponse
import com.zhdanon.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.userRoutes(userRepository: UserRepository) {
    routing {
        authenticate("auth-jwt") {
            get("/user/profile") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim("userId").asString()

                val user = userRepository.findById(UUID.fromString(userId))
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(
                    UserResponse(
                        id = user.id.toString(),
                        email = user.email,
                        role = user.role
                    )
                )
            }
        }
    }
}