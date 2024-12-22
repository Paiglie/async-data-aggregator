package org.paiglie

import io.ktor.http.*
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.paiglie.config.applicationModules
import org.paiglie.config.applyDefaultConfiguration
import org.paiglie.controller.model.ErrorResponse
import org.paiglie.controller.usersController
import org.paiglie.exception.Error

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(applicationModules)
    }

    install(ContentNegotiation) {
        jackson{
            applyDefaultConfiguration()
        }
    }

    usersController()

    install(StatusPages) {
        unhandled { call ->
            call.respond(HttpStatusCode.NotFound, ErrorResponse.from(Error.resourceNotFound()))
        }
        exception<Error> { call: ApplicationCall, cause: Error ->
            call.respond(HttpStatusCode.fromValue(cause.status), ErrorResponse.from(cause))

        }
        exception<Throwable> { call: ApplicationCall, cause: Throwable ->
            logError(call, cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse.from(Error.unexpectedError()))
        }
    }

}
