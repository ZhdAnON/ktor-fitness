package com.zhdanon.routes

import com.zhdanon.models.mappers.toResponse
import com.zhdanon.models.request.CreateNutritionProgramRequest
import com.zhdanon.repository.NutritionRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.nutritionRoutes(repo: NutritionRepository) {
    routing {

        // Получить все программы питания
        get("/nutrition") {
            val programs = repo.getAllPrograms()
            call.respond(programs.map { it.toResponse() })
        }

        // Получить одну программу по id
        get("/nutrition/{id}") {
            val idParam = call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "ID is required")

            val id = try {
                UUID.fromString(idParam)
            } catch (e: Exception) {
                return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
            }

            val program = repo.getProgramById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound, "Program not found")

            call.respond(program.toResponse())
        }

        authenticate("auth-jwt") {

            // Создать программу питания (только админ)
            post("/nutrition") {
                val principal = call.principal<JWTPrincipal>()!!
                val role = principal.payload.getClaim("role").asString()

                if (role != "ADMIN") {
                    return@post call.respond(HttpStatusCode.Forbidden, "Admin only")
                }

                val request = call.receive<CreateNutritionProgramRequest>()
                val program = repo.createProgram(request)

                call.respond(program.toResponse())
            }

            // Удалить программу питания (только админ)
            delete("/nutrition/{id}") {
                val idParam = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID is required")
                val id = try {
                    UUID.fromString(idParam)
                } catch (e: Exception) {
                    return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                }

                val deleted = repo.deleteProgram(id)
                if (deleted) call.respond(HttpStatusCode.OK, "Program deleted")
                else call.respond(HttpStatusCode.NotFound, "Program not found")
            }
        }
    }
}