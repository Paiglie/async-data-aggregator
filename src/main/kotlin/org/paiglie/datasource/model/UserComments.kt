package org.paiglie.datasource.model

data class UserComment(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
)
