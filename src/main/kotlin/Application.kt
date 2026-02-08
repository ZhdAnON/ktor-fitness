package com.zhdanon

import com.zhdanon.plugins.configureRouting
import com.zhdanon.plugins.configureSecurity
import com.zhdanon.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureRouting()
}