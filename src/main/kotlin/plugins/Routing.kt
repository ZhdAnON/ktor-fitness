package com.zhdanon.plugins

import com.zhdanon.auth.JwtService
import com.zhdanon.auth.authRoutes
import com.zhdanon.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val repo = UserRepository()

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }

        route("/auth") {
            val jwt = JwtService(environment?.config ?: error("Application config not loaded"))
            authRoutes(jwt, repo)
        }

        authenticate("auth-jwt") {
            get("/profile") {
                call.respondText("This is a protected endpoint")
            }
        }
    }
}