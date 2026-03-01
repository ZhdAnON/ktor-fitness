package com.zhdanon

import com.zhdanon.auth.AuthService
import com.zhdanon.auth.JwtConfig
import com.zhdanon.auth.configureAuth
import com.zhdanon.database.DatabaseFactory
import com.zhdanon.repository.ExerciseRepository
import com.zhdanon.repository.NutritionRepository
import com.zhdanon.repository.WorkoutsRepository
import com.zhdanon.routes.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {
    val config = JwtConfig
    val service = AuthService(jwtConfig = config)
    val workoutsRepository = WorkoutsRepository()
    val exerciseRepository = ExerciseRepository()
    val nutritionRepository = NutritionRepository()

    val bucketName = "fitness-app-storage"

    val s3 = S3Client.builder()
        .region(Region.of("ru-central1"))
        .endpointOverride(URI("https://storage.yandexcloud.net"))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    System.getenv("YC_ACCESS_KEY"),
                    System.getenv("YC_SECRET_KEY")
                )
            )
        )
        .build()

    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
                explicitNulls = false
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
    adminUserRoutes(service.userRepository)
    userRoutes(service.userRepository)
    workoutRoutes(workoutsRepository)
    exerciseRoutes(exerciseRepository, s3, bucketName)
    nutritionRoutes(nutritionRepository)
}