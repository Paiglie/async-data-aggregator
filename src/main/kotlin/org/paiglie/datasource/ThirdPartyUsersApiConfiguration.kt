package org.paiglie.datasource

import io.ktor.serialization.jackson.jackson
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import org.paiglie.config.applyDefaultConfiguration
import org.paiglie.exception.Error

internal fun thirdPartyUsersHttpClient(engine: HttpClientEngine) = HttpClient(engine) {
    expectSuccess = true
    install(ContentNegotiation) {
        jackson {
            applyDefaultConfiguration()
        }
    }
    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            val clientException = cause as? ClientRequestException ?: return@handleResponseExceptionWithRequest
            val exceptionResponse = clientException.response
            if (exceptionResponse.status == HttpStatusCode.NotFound) {
                throw Error.userNotFound()
            }
        }
    }

    defaultRequest {
        url("https://jsonplaceholder.typicode.com/")
    }
}

fun defaultThirdPartyUsersHttpClient() = thirdPartyUsersHttpClient(CIO.create())
