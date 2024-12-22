package org.paiglie.datasource

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.paiglie.datasource.model.UserData

class UserDataApiClient(
    private val httpClient: HttpClient
) {

    suspend fun retrieveUserDataByUserId(userId: Int) =
        httpClient
            .get("users/$userId")
            .body<UserData>()
}
