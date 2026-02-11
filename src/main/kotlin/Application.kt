package com.zhdanon

import com.zhdanon.auth.AuthService
import com.zhdanon.auth.JwtConfig
import com.zhdanon.auth.configureAuth
import com.zhdanon.database.DatabaseFactory
import com.zhdanon.routes.authRoutes
import com.zhdanon.routes.healthRoutes
import com.zhdanon.routes.userRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    val config = JwtConfig
    val service = AuthService(jwtConfig = config)
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module(service)
    }.start(wait = true)
}

fun Application.module(service: AuthService) {
    install(ContentNegotiation) { json() }
    DatabaseFactory.init()
    configureAuth()
    healthRoutes()
    authRoutes(service)
    userRoutes(service.userRepository)
}