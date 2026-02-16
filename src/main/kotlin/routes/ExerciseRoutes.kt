package com.zhdanon.routes

import com.zhdanon.models.mappers.toResponse
import com.zhdanon.models.request.ExerciseRequest
import com.zhdanon.repository.ExerciseRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.exerciseRoutes(repo: ExerciseRepository) {
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

            post {
                val req = call.receive<ExerciseRequest>()
                val created = repo.create(req)
                call.respond(HttpStatusCode.Created, created.toResponse())
            }

            put("{id}") {
                val id = call.parameters["id"]?.let(UUID::fromString)
                    ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                val req = call.receive<ExerciseRequest>()
                val updated = repo.update(id, req)

                if (updated) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound)
            }

            delete("{id}") {
                val id = call.parameters["id"]?.let(UUID::fromString)
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")

                val deleted = repo.delete(id)

                if (deleted) call.respond(HttpStatusCode.OK)
                else call.respond(HttpStatusCode.NotFound)
            }
        }
    }

}