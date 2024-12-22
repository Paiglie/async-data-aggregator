package org.paiglie.datasource

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.paiglie.exception.Error
import org.paiglie.datasource.model.UserData
import org.paiglie.datasource.model.UserDataAddress
import org.paiglie.datasource.model.UserDataAddressGeoLocation
import org.paiglie.datasource.model.UserDataCompany

internal class UserDataApiClientTest {
    @Test
    fun `retrieving userData should succeed`() {
        val mockEngine = MockEngine {
            respond(
                content = SUCCESS_RESPONSE,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val sut = UserDataApiClient(thirdPartyUsersHttpClient(mockEngine))

        runTest {
            val response = sut.retrieveUserDataByUserId(USER_ID_1)

            Assertions.assertEquals(EXPECTED, response)
        }
    }

    @Test
    fun `retrieving userData should return the correct error if the user couldn't be found`() {
        val mockEngine = MockEngine {
            respond(
                content = "{}",
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val sut = UserDataApiClient(thirdPartyUsersHttpClient(mockEngine))

        runTest {
            val exception = assertThrows<Error> {
                sut.retrieveUserDataByUserId(USER_ID_1)
            }

            Assertions.assertEquals(Error.userNotFound(), exception)
        }
    }

    private companion object {
        const val USER_ID_1: Int = 1

        val SUCCESS_RESPONSE = """
               {
                 "id": 1,
                 "name": "Leanne Graham",
                 "username": "Bret",
                 "email": "Sincere@april.biz",
                 "address": {
                   "street": "Kulas Light",
                   "suite": "Apt. 556",
                   "city": "Gwenborough",
                   "zipcode": "92998-3874",
                   "geo": {
                     "lat": "-37.3159",
                     "lng": "81.1496"
                   }
                 },
                 "phone": "1-770-736-8031 x56442",
                 "website": "hildegard.org",
                 "company": {
                   "name": "Romaguera-Crona",
                   "catchPhrase": "Multi-layered client-server neural-net",
                   "bs": "harness real-time e-markets"
                 }
               }
           """.trimIndent()

        val EXPECTED = UserData(
            USER_ID_1,
            "Leanne Graham",
            "Bret",
            "Sincere@april.biz",
            UserDataAddress(
                "Kulas Light",
                "Apt. 556",
                "Gwenborough",
                "92998-3874",
                UserDataAddressGeoLocation(
                    "-37.3159",
                    "81.1496",
                ),
            ),
            "1-770-736-8031 x56442",
            "hildegard.org",
            UserDataCompany(
                "Romaguera-Crona",
                "Multi-layered client-server neural-net",
                "harness real-time e-markets",
            ),
        )
    }
}
