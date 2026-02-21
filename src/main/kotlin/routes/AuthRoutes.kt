package com.zhdanon.routes

import com.zhdanon.auth.*
import com.zhdanon.models.mappers.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoutes(authService: AuthService) {
    routing {
        post("/auth/register") {
            val req = call.receive<AuthRequest>()
            val result = authService.register(req.email, req.password)

            result.fold(
                onSuccess = { (user, tokens) ->
                    call.respond(
                        AuthResponse(
                            user = user.toResponse(),
                            accessToken = tokens.access,
                            refreshToken = tokens.refresh
                        )
                    )
                },
                onFailure = {
                    call.respondText(
                        it.message ?: "Error",
                        status = HttpStatusCode.BadRequest
                    )
                }
            )
        }

        post("/auth/login") {
            val req = call.receive<AuthRequest>()
            val result = authService.login(req.email, req.password)

            result.fold(
                onSuccess = { (user, tokens) ->
                    call.respond(
                        AuthResponse(
                            user = user.toResponse(),
                            accessToken = tokens.access,
                            refreshToken = tokens.refresh
                        )
                    )
                },
                onFailure = {
                    call.respondText(
                        it.message ?: "Error",
                        status = HttpStatusCode.BadRequest
                    )
                }
            )
        }

        post("/auth/refresh") {
            try {
                val body = call.receive<RefreshRequest>()
                val response = authService.refresh(body.refreshToken)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid refresh token")
            }
        }
    }
}