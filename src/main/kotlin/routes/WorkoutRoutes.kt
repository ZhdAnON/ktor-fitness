package com.zhdanon.routes

import com.zhdanon.models.dto.WorkoutRequest
import com.zhdanon.models.mappers.toDomain
import com.zhdanon.models.mappers.toResponse
import com.zhdanon.repository.WorkoutsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.workoutRoutes(repo: WorkoutsRepository) {
    routing {
        authenticate("auth-jwt") {
            post("/workout") {
                val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()
                val request = call.receive<WorkoutRequest>()
                val workoutId = UUID.randomUUID()
                val workout = request.toDomain(workoutId)

                repo.createWorkout(UUID.fromString(userId), workout)
                call.respond(HttpStatusCode.OK)
            }

            get("/workout") {
                val workouts = repo.getWorkouts()
                call.respond(workouts.map { it.toResponse() })
            }

            get("/workout/{id}") {
                val workoutIdParam = call.parameters["id"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Workout ID is required")

                val workoutId = try {
                    UUID.fromString(workoutIdParam)
                } catch (e: IllegalArgumentException) {
                    return@get call.respond(HttpStatusCode.BadRequest, "Invalid workout ID format")
                }

                val workout = repo.getWorkoutById(workoutId)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Workout not found")

                call.respond(workout.toResponse())
            }


            delete("/workout/{id}") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = UUID.fromString(principal.payload.getClaim("userId").asString())

                val workoutIdParam = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "Workout ID is required")

                val workoutId = try {
                    UUID.fromString(workoutIdParam)
                } catch (e: IllegalArgumentException) {
                    return@delete call.respond(HttpStatusCode.BadRequest, "Invalid workout ID format")
                }

                val deleted = repo.deleteWorkout(userId, workoutId)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, "Workout deleted")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Workout not found")
                }
            }

            put("/workout/{id}") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = UUID.fromString(principal.payload.getClaim("userId").asString())

                val workoutId = UUID.fromString(call.parameters["id"]!!)

                val request = call.receive<WorkoutRequest>()
                val workout = request.toDomain(workoutId)

                val updated = repo.updateWorkout(userId, workoutId, workout)

                if (updated) call.respond("Workout updated")
                else call.respond(HttpStatusCode.NotFound, "Workout not found")
            }
        }
    }
}