package com.zhdanon.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.authRoutes() {
    val authService = AuthService()

    routing {
        post("/auth/register") {
            val raw = call.receiveText()
            println("RAW BODY = '$raw'")
            val req = Json.decodeFromString<AuthRequest>(raw)

//            val req = call.receive<AuthRequest>()
            val result = authService.register(req.email, req.password)

            result.fold(
                onSuccess = { (user, tokens) ->
                    call.respond(
                        AuthResponse(
                            user = UserResponse(
                                id = user.id.toString(),
                                email = user.email,
                                role = user.role
                            ),
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
                            user = UserResponse(
                                id = user.id.toString(),
                                email = user.email,
                                role = user.role
                            ),
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
    }
}