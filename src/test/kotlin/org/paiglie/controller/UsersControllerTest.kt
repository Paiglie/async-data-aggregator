package org.paiglie.controller

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockkClass
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock
import org.paiglie.exception.Error
import org.paiglie.module
import org.paiglie.service.UsersService
import org.paiglie.service.model.*

internal class UsersControllerTest: KoinTest {

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }


    @Test
    fun `retrieving a user should succeed`() = testApplication {
        application {
            module()
            declareMock<UsersService> {
                coEvery { retrieveUserById(1) }.coAnswers { USERS_RESPONSE }
            }
        }

        val response = client.get("/users/1")
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals(OK_EXPECTED_RESPONSE, response.bodyAsText())
    }

    @Test
    fun `retrieving a user for an invalid userId should return an error`() = testApplication {
        application {
            module()
        }

        val response = client.get("/users/a")
        Assertions.assertEquals(HttpStatusCode.BadRequest, response.status)
        Assertions.assertEquals(BAD_REQUEST_EXPECTED_RESPONSE, response.bodyAsText())
    }

    @Test
    fun `retrieving a non existent user should return an error`() = testApplication {
        application {
            module()
            declareMock<UsersService> {
                coEvery { retrieveUserById(100) }.coAnswers { throw Error.userNotFound() }
            }
        }

        val response = client.get("/users/100")
        Assertions.assertEquals(HttpStatusCode.NotFound, response.status)
        Assertions.assertEquals(USER_NOT_FOUND_EXPECTED_RESPONSE, response.bodyAsText())
    }

    @Test
    fun `retrieving a user should return an error when an unexpected error occurs`() = testApplication {
        application {
            module()
            declareMock<UsersService> {
                coEvery { retrieveUserById(USER_ID_1) }.coAnswers { throw Error.unexpectedError() }
            }
        }

        val response = client.get("/users/1")
        Assertions.assertEquals(HttpStatusCode.InternalServerError, response.status)
        Assertions.assertEquals(UNEXPECTED_ERROR_EXPECTED_RESPONSE, response.bodyAsText())
    }

    @Test
    fun `trying to retrieve an unknown resource should return an error`() = testApplication {
        application {
            module()
        }

        val response = client.get("/users")
        Assertions.assertEquals(HttpStatusCode.NotFound, response.status)
        Assertions.assertEquals(RESOURCE_NOT_FOUND_EXPECTED_RESPONSE, response.bodyAsText())
    }

    private companion object {
        const val USER_ID_1: Int = 1

        val USERS_RESPONSE = User(
            USER_ID_1,
            UserData(
                "some-name",
                "some-username",
                "some-email",
                UserDataAddress(
                    "some-street",
                    "some-suite",
                    "some-city",
                    "some-zipcode",
                    UserDataAddressGeoLocation(
                        "some-lat",
                        "some-lng",
                    ),
                ),
                "some-phone",
                "some-website",
                UserDataCompany(
                    "some-company-name",
                    "some-catchphrase",
                    "some-bs",
                ),
            ),
            listOf(
                UserComment(
                    1,
                    "some-title",
                    "some-body",
                ),
                UserComment(
                    2,
                    "some-title-2",
                    "some-body-2",
                ),
                UserComment(
                    3,
                    "some-title-3",
                    "some-body-3",
                )
            )
        )

        val OK_EXPECTED_RESPONSE = """
          {"userId":1,"data":{"name":"some-name","username":"some-username","email":"some-email","address":{"street":"some-street","suite":"some-suite","city":"some-city","zipcode":"some-zipcode","geo":{"lat":"some-lat","lng":"some-lng"}},"phone":"some-phone","website":"some-website","company":{"name":"some-company-name","catchPhrase":"some-catchphrase","bs":"some-bs"}},"comments":[{"id":1,"title":"some-title","body":"some-body"},{"id":2,"title":"some-title-2","body":"some-body-2"},{"id":3,"title":"some-title-3","body":"some-body-3"}]}
        """.trimIndent()

        val BAD_REQUEST_EXPECTED_RESPONSE = """
          {"status":"400","title":"missing-parameter","detail":"Please provide a valid userId."}
        """.trimIndent()

        val USER_NOT_FOUND_EXPECTED_RESPONSE = """
          {"status":"404","title":"user-not-found","detail":"The user with the provided userId could not be found."}
        """.trimIndent()

        val RESOURCE_NOT_FOUND_EXPECTED_RESPONSE = """
          {"status":"404","title":"resource-not-found","detail":"The resource you were searching for doesn't exists."}
        """.trimIndent()

        val UNEXPECTED_ERROR_EXPECTED_RESPONSE = """
          {"status":"500","title":"unexpected-error","detail":"An unexpected Error occurred please try again later."}
        """.trimIndent()
    }
}
