package org.paiglie.datasource

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.paiglie.datasource.model.UserComment

class UserCommentsApiClient(
    private val httpClient: HttpClient
) {

    suspend fun retrieveCommentsByUserId(userId: Int) =
        httpClient
            .get("posts") {
                parameter("userId", userId)
            }.body<List<UserComment>>()
}
