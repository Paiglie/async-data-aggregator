package org.paiglie.datasource

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.paiglie.datasource.model.UserComment
import org.paiglie.exception.Error

internal class UserCommentsApiClientTest{
    @Test
    fun `retrieving comments should succeed`() {
        val mockEngine = MockEngine {
            respond(
                content = SUCCESS_RESPONSE,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val sut = UserCommentsApiClient(thirdPartyUsersHttpClient(mockEngine))

        runTest {
            val response = sut.retrieveCommentsByUserId(USER_ID_1)

            Assertions.assertEquals(EXPECTED, response)
        }
    }

    @Test
    fun `retrieving comments should return the correct error if the user couldn't be found`() {
        val mockEngine = MockEngine {
            respond(
                content = "{}",
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val sut = UserCommentsApiClient(thirdPartyUsersHttpClient(mockEngine))

        runTest {
            val exception = assertThrows<Error> {
                sut.retrieveCommentsByUserId(USER_ID_1)
            }

            Assertions.assertEquals(Error.userNotFound(), exception)
        }
    }

    private companion object {
        const val USER_ID_1: Int = 1

        val SUCCESS_RESPONSE = """
            [
              {
                "userId": 1,
                "id": 1,
                "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
              },
              {
                "userId": 1,
                "id": 2,
                "title": "qui est esse",
                "body": "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla"
              }
          ]
        """.trimIndent()

        val EXPECTED = listOf(
            UserComment(
                USER_ID_1,
                1,
                "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
            ),
            UserComment(
                USER_ID_1,
                2,
                "qui est esse",
                "est rerum tempore vitae\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\nqui aperiam non debitis possimus qui neque nisi nulla"
            ),
        )
    }
}
