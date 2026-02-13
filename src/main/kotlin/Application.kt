package com.zhdanon

import com.zhdanon.auth.AuthService
import com.zhdanon.auth.JwtConfig
import com.zhdanon.auth.configureAuth
import com.zhdanon.database.DatabaseFactory
import com.zhdanon.repository.WorkoutsRepository
import com.zhdanon.routes.authRoutes
import com.zhdanon.routes.healthRoutes
import com.zhdanon.routes.userRoutes
import com.zhdanon.routes.workoutRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*   // функция json()
import kotlinx.serialization.json.Json
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    val config = JwtConfig
    val service = AuthService(jwtConfig = config)
    val workoutsRepository = WorkoutsRepository()

    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(CallLogging) {
        level = Level.INFO
    }

    install(StatusPages) {
        exception<Throwable> { _, cause ->
            this@module.environment.log.error("Unhandled exception", cause)
            throw cause
        }
    }

    DatabaseFactory.init()
    configureAuth()
    healthRoutes()
    authRoutes(service)
    userRoutes(service.userRepository)
    workoutRoutes(workoutsRepository)
}