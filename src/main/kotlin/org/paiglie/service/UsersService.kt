package org.paiglie.service

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.paiglie.datasource.UserCommentsApiClient
import org.paiglie.datasource.UserDataApiClient
import org.paiglie.service.model.User

class UsersService(
    private val dataApi: UserDataApiClient,
    private val commentsApi: UserCommentsApiClient,
) {

    suspend fun retrieveUserById(userId: Int) = coroutineScope {
        val userDataResult = async {
            dataApi.retrieveUserDataByUserId(userId)
        }
        val userCommentsResult = async {
            commentsApi.retrieveCommentsByUserId(userId)
        }

        val userData = userDataResult.await()
        val userComments = userCommentsResult.await()

        User.from(userData, userComments)
    }
}
