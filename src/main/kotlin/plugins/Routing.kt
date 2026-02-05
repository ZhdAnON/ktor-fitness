package com.zhdanon.plugins

import com.zhdanon.auth.JwtService
import com.zhdanon.auth.authRoutes
import com.zhdanon.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val jwt = JwtService(environment.config)
    val repo = UserRepository()

    routing {
        // Public
        route("/auth") {
            authRoutes(jwt, repo)
        }

        // Protected
        authenticate {
            get("/profile") {
                call.respondText("This is a protected endpoint")
            }
        }
    }
}