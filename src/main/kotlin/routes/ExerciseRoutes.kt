package com.zhdanon.routes

import com.zhdanon.models.mappers.toResponse
import com.zhdanon.models.request.ExerciseRequest
import com.zhdanon.repository.ExerciseRepository
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.*

fun Application.exerciseRoutes(
    repo: ExerciseRepository,
    s3: S3Client,
    bucketName: String
) {
    routing {
        route("/exercises") {

            // Получить все упражнения
            get {
                val exercises = repo.getAll()
                call.respond(exercises.map { it.toResponse() })
            }

            // Получить упражнение по ID
            get("{id}") {
                val id = call.parameters["id"]?.let(UUID::fromString)
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                val exercise = repo.getById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Exercise not found")

                call.respond(exercise.toResponse())
            }

            // Создать упражнение (без видео)
            post {
                val req = call.receive<ExerciseRequest>()
                val created = repo.create(req)
                call.respond(HttpStatusCode.Created, created.toResponse())
            }

            // Обновить упражнение (без видео)
            put("{id}") {
                val id = call.parameters["id"]?.let(UUID::fromString)
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                val req = call.receive<ExerciseRequest>()
                val updated = repo.update(id, req)

                if (updated) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound)
            }

            // Удалить упражнение
            delete("{id}") {
                val id = call.parameters["id"]?.let(UUID::fromString)
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                val deleted = repo.delete(id)

                if (deleted) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound)
            }

            // 🔥 Загрузка видео для упражнения
            post("{id}/video") {
                val id = call.parameters["id"]?.let(UUID::fromString)
                    ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                if (repo.getById(id) == null) {
                    return@post call.respond(HttpStatusCode.NotFound, "Exercise not found")
                }

                val multipart = call.receiveMultipart()
                var fileBytes: ByteArray? = null

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        fileBytes = part.streamProvider().readBytes()
                    }
                    part.dispose()
                }

                if (fileBytes == null) {
                    return@post call.respond(HttpStatusCode.BadRequest, "No video file provided")
                }

                // Ключ в бакете
                val key = "videos/${id}.mp4"

                // Загрузка в Object Storage
                s3.putObject(
                    PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("video/mp4")
                        .build(),
                    RequestBody.fromBytes(fileBytes!!)
                )

                // Публичный URL (если бакет публичный)
                val videoUrl = "https://storage.yandexcloud.net/$bucketName/$key"

                // Сохранение URL в базе
                repo.updateVideoUrl(id, videoUrl)

                call.respond(HttpStatusCode.OK, mapOf("videoUrl" to videoUrl))
            }
        }
    }
}