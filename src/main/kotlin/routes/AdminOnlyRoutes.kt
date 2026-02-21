package com.zhdanon.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminOnly(block: Route.() -> Unit) {
    intercept(ApplicationCallPipeline.Call) {
        val principal = call.principal<JWTPrincipal>()
            ?: return@intercept call.respond(HttpStatusCode.Unauthorized)

        val role = principal.payload.getClaim("role").asString()

        if (role.uppercase() != "ADMIN") {
            call.respond(HttpStatusCode.Forbidden, "Admin only")
            finish()
        }
    }
    block()
}