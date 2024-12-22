package org.paiglie.service

import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.paiglie.datasource.UserCommentsApiClient
import org.paiglie.datasource.UserDataApiClient
import org.paiglie.exception.Error
import org.paiglie.service.model.*
import org.paiglie.datasource.model.UserData as UserDataDataSourceModel
import org.paiglie.datasource.model.UserComment as UserCommentDataSourceModel
import org.paiglie.datasource.model.UserDataAddress as UserDataAddressDataSourceModel
import org.paiglie.datasource.model.UserDataAddressGeoLocation as UserDataAddressGeoLocationDataSourceModel
import org.paiglie.datasource.model.UserDataCompany as UserDataCompanyDataSourceModel

internal class UsersServiceTest {

    private val userDataApi: UserDataApiClient = mockk()
    private val userCommentsApi: UserCommentsApiClient = mockk()

    private val sut = UsersService(userDataApi, userCommentsApi)

    @BeforeEach
    fun reset(){
        clearMocks(userDataApi, userCommentsApi)

        coEvery { userDataApi.retrieveUserDataByUserId(USER_ID_1) }.coAnswers {
            delay(10)
            USER_DATA
        }
        coEvery { userCommentsApi.retrieveCommentsByUserId(USER_ID_1) }.coAnswers {
            delay(10)
            USER_COMMENTS
        }
    }

    @Test
    fun `retrieving a user should succeed`(){
        runTest{
            val response = sut.retrieveUserById(USER_ID_1)

            Assertions.assertEquals(EXPECTED_USER, response)
        }

        coVerify {
            userDataApi.retrieveUserDataByUserId(USER_ID_1)
            userCommentsApi.retrieveCommentsByUserId(USER_ID_1)
        }
     }

    @Test
    fun `retrieving a user should propagate the error if the userData can't get retrieved`(){
        val originalError = Error.userNotFound()

        coEvery { userDataApi.retrieveUserDataByUserId(USER_ID_1) }.coAnswers {
            delay(10)
            throw originalError
        }

        runTest{
            val exception = assertThrows<Error> {
                sut.retrieveUserById(USER_ID_1)
            }

            Assertions.assertEquals(originalError, exception)
        }

        coVerify {
            userDataApi.retrieveUserDataByUserId(USER_ID_1)
            userCommentsApi.retrieveCommentsByUserId(USER_ID_1)
        }
    }

    @Test
    fun `retrieving a user should propagate the error if the userComments can't get retrieved`(){
        val originalError = Error.userNotFound()

        coEvery { userCommentsApi.retrieveCommentsByUserId(USER_ID_1) }.coAnswers {
            delay(10)
            throw originalError
        }

        runTest{
            val exception = assertThrows<Error> {
                sut.retrieveUserById(USER_ID_1)
            }

            Assertions.assertEquals(originalError, exception)
        }

        coVerify {
            userDataApi.retrieveUserDataByUserId(USER_ID_1)
            userCommentsApi.retrieveCommentsByUserId(USER_ID_1)
        }
    }

    private companion object {
        const val USER_ID_1: Int = 1

        val USER_DATA = UserDataDataSourceModel(
            1,
            "some-name",
            "some-username",
            "some-email",
            UserDataAddressDataSourceModel(
                "some-street",
                "some-suite",
                "some-city",
                "some-zipcode",
                UserDataAddressGeoLocationDataSourceModel(
                    "some-lat",
                    "some-lng",
                ),
            ),
            "some-phone",
            "some-website",
            UserDataCompanyDataSourceModel(
                "some-company-name",
                "some-catchphrase",
                "some-bs",
            ),
        )

        val USER_COMMENTS = listOf(
            UserCommentDataSourceModel(
                USER_ID_1,
                1,
                "some-title",
                "some-body",
            ),
            UserCommentDataSourceModel(
                USER_ID_1,
                2,
                "some-title-2",
                "some-body-2",
            ),
            UserCommentDataSourceModel(
                USER_ID_1,
                3,
                "some-title-3",
                "some-body-3",
            )
        )

        val EXPECTED_USER = User(
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
    }
}
