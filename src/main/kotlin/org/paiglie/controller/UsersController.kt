package org.paiglie.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.paiglie.exception.Error
import org.paiglie.controller.model.User
import org.paiglie.service.UsersService

fun Application.usersController() {
    val service by inject<UsersService>()

    routing {
        get("/users/{userId}") {
            val userId = call.parameters["userId"]
            if(userId.isNullOrEmpty() || userId.toIntOrNull() == null){
                throw Error.badRequestUserId()
            }

            val response = service.retrieveUserById(userId.toInt())

            call.respond(HttpStatusCode.OK, User.from(response))
        }
    }
}
