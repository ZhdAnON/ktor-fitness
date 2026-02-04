package com.zhdanon.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val config = environment.config.config("ktor.database")

    Database.connect(
        url = config.property("url").getString(),
        driver = "org.postgresql.Driver",
        user = config.property("user").getString(),
        password = config.property("password").getString()
    )
}