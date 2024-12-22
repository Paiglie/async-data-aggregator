package org.paiglie.exception

data class Error(
    val status: Int,
    val title: String,
    val detail: String,
): Throwable(){
    companion object {
        fun badRequestUserId() = Error(
            status = 400,
            title = "missing-parameter",
            detail = "Please provide a valid userId."
        )

        fun userNotFound() = Error(
            status = 404,
            title = "user-not-found",
            detail = "The user with the provided userId could not be found.",
        )

        fun resourceNotFound() = Error(
            status = 404,
            title = "resource-not-found",
            detail = "The resource you were searching for doesn't exists.",
        )

        fun unexpectedError() = Error(
            status = 500,
            title = "unexpected-error",
            detail = "An unexpected Error occurred please try again later."
        )
    }
}
