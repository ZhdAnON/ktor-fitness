package com.zhdanon.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.healthRoutes() {
    routing {
        get("/health") {
            call.respondText("OK")
        }
    }
}