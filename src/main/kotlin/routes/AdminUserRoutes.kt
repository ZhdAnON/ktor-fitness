package com.zhdanon.routes

import com.zhdanon.models.mappers.toResponse
import com.zhdanon.models.response.AddUserRequestDto
import com.zhdanon.models.response.UpdatePasswordRequestDto
import com.zhdanon.models.response.UpdateRoleRequestDto
import com.zhdanon.repository.UserRepository
import com.zhdanon.utils.hashPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.adminUserRoutes(userRepository: UserRepository) {
    routing {
        authenticate("auth-jwt") {
            adminOnly {

                get("/admin/users") {
                    val users = userRepository.getAll()
                    call.respond(users.map { it.toResponse() })
                }

                post("/admin/users") {
                    val req = call.receive<AddUserRequestDto>()

                    if (userRepository.emailExists(req.email)) {
                        return@post call.respond(HttpStatusCode.Conflict, "Email already exists")
                    }

                    val hash = hashPassword(req.password)
                    val user = userRepository.createUser(req.email, hash, req.role.uppercase())

                    call.respond(user.toResponse())
                }

                put("/admin/users/{id}/role") {
                    val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val req = call.receive<UpdateRoleRequestDto>()

                    userRepository.updateRole(UUID.fromString(id), req.role.uppercase())
                    call.respond(HttpStatusCode.OK)
                }

                put("/admin/users/{id}/password") {
                    val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                    val req = call.receive<UpdatePasswordRequestDto>()

                    val hash = hashPassword(req.password)
                    userRepository.updatePassword(UUID.fromString(id), hash)

                    call.respond(HttpStatusCode.OK)
                }

                delete("/admin/users/{id}") {
                    val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    userRepository.delete(UUID.fromString(id))
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}