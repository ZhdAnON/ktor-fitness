package com.zhdanon

import com.zhdanon.plugins.configureDatabase
import com.zhdanon.plugins.configureRouting
import com.zhdanon.plugins.configureSecurity
import com.zhdanon.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureDatabase()
    configureSecurity()
    configureRouting()
}