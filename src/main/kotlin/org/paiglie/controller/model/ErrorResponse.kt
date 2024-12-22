package org.paiglie.controller.model

import org.paiglie.exception.Error

data class ErrorResponse(
    val status: String,
    val title: String,
    val detail: String,
) {
    companion object {
        fun from(mappingObject: Error): ErrorResponse {
            return ErrorResponse(
                status = mappingObject.status.toString(),
                title = mappingObject.title,
                detail = mappingObject.detail,
            )
        }
    }
}
