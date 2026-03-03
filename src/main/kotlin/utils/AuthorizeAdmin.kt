package com.zhdanon.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*

suspend fun PipelineContext<Unit, ApplicationCall>.authorizeAdmin() {
    val principal = call.principal<JWTPrincipal>()
        ?: return call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
    val role = principal.payload.getClaim("role").asString()
    if (role != "ADMIN") {
        call.respond(HttpStatusCode.Forbidden, "Admin only")
        finish()
    }
}