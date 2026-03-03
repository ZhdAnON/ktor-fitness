package com.zhdanon.routes

import com.zhdanon.models.mappers.toResponse
import com.zhdanon.models.request.ExerciseRequest
import com.zhdanon.repository.ExerciseRepository
import com.zhdanon.utils.authorizeAdmin
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

            get {
                val exercises = repo.getAll()
                call.respond(exercises.map { it.toResponse() })
            }

            get("{id}") {
                val id = call.parameters["id"]?.let(UUID::fromString)
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                val exercise = repo.getById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Exercise not found")

                call.respond(exercise.toResponse())
            }

            authenticate("auth-jwt") {

                put("{id}") {
                    authorizeAdmin()

                    val id = call.parameters["id"]?.let(UUID::fromString)
                        ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                    val req = call.receive<ExerciseRequest>()
                    val updated = repo.update(id, req)

                    if (!updated) {
                        return@put call.respond(HttpStatusCode.NotFound)
                    }

                    val exercise = repo.getById(id)!!
                    call.respond(exercise.toResponse())
                }

                post {
                    authorizeAdmin()

                    val req = call.receive<ExerciseRequest>()
                    val created = repo.create(req)

                    call.respond(HttpStatusCode.Created, created.toResponse())
                }

                delete("{id}") {
                    authorizeAdmin()

                    val id = call.parameters["id"]?.let(UUID::fromString)
                        ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                    val deleted = repo.delete(id)

                    if (deleted) call.respond(HttpStatusCode.OK)
                    else call.respond(HttpStatusCode.NotFound)
                }

                post("{id}/video") {
                    authorizeAdmin()

                    val id = call.parameters["id"]?.let(UUID::fromString)
                        ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                    val exercise = repo.getById(id)
                        ?: return@post call.respond(HttpStatusCode.NotFound, "Exercise not found")

                    val multipart = call.receiveMultipart()
                    var fileBytes: ByteArray? = null
                    var fileName: String? = null

                    multipart.forEachPart { part ->
                        if (part is PartData.FileItem) {
                            fileBytes = part.streamProvider().readBytes()
                            fileName = part.originalFileName
                        }
                        part.dispose()
                    }

                    if (fileBytes == null) {
                        return@post call.respond(HttpStatusCode.BadRequest, "No video file provided")
                    }

                    val safeName = fileName ?: "${id}.mp4"
                    val key = "videos/$id/$safeName"

                    s3.putObject(
                        PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType("video/mp4")
                            .build(),
                        RequestBody.fromBytes(fileBytes!!)
                    )

                    val videoUrl = "https://storage.yandexcloud.net/$bucketName/$key"
                    repo.updateVideoUrl(id, videoUrl)

                    call.respond(HttpStatusCode.OK, mapOf("videoUrl" to videoUrl))
                }
            }
        }
    }
}