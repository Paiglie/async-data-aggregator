package org.paiglie.datasource.model

data class UserData(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: UserDataAddress,
    val phone: String,
    val website: String,
    val company: UserDataCompany,
)

data class UserDataAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: UserDataAddressGeoLocation,
)

data class UserDataAddressGeoLocation(
    val lat: String,
    val lng: String,
)

data class UserDataCompany(
    val name: String,
    val catchPhrase: String,
    val bs: String,
)
