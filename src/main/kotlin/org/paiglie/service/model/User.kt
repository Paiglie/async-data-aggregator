package org.paiglie.service.model

import org.paiglie.datasource.model.UserData as UserDataDataSourceModel
import org.paiglie.datasource.model.UserComment as UserCommentDataSourceModel
import org.paiglie.datasource.model.UserDataAddress as UserDataAddressDataSourceModel
import org.paiglie.datasource.model.UserDataAddressGeoLocation as UserDataAddressGeoLocationDataSourceModel
import org.paiglie.datasource.model.UserDataCompany as UserDataCompanyDataSourceModel

data class User(
    val userId: Int,
    val data: UserData,
    val comments: List<UserComment>
){
    companion object {
        fun from(userData:UserDataDataSourceModel, comments: List<UserCommentDataSourceModel>): User{
            return User(
                userId = userData.id,
                data = UserData.from(userData),
                comments = UserComment.from(comments)
            )
        }
    }
}

data class UserData(
    val name: String,
    val username: String,
    val email: String,
    val address: UserDataAddress,
    val phone: String,
    val website: String,
    val company: UserDataCompany,
){
    companion object {
        fun from(mappingObject: UserDataDataSourceModel): UserData {
            return UserData(
                name = mappingObject.name,
                username = mappingObject.username,
                email = mappingObject.email,
                address = UserDataAddress.from(mappingObject.address),
                phone = mappingObject.phone,
                website = mappingObject.website,
                company = UserDataCompany.from(mappingObject.company),
            )
        }
    }
}

data class UserDataAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: UserDataAddressGeoLocation,
){
    companion object {
        fun from(mappingObject: UserDataAddressDataSourceModel): UserDataAddress{
            return UserDataAddress(
                street = mappingObject.street,
                suite = mappingObject.suite,
                city = mappingObject.city,
                zipcode = mappingObject.zipcode,
                geo = UserDataAddressGeoLocation.from(mappingObject.geo),
            )
        }
    }
}

data class UserDataAddressGeoLocation(
    val lat: String,
    val lng: String,
){
    companion object {
        fun from(mappingObject: UserDataAddressGeoLocationDataSourceModel): UserDataAddressGeoLocation {
            return UserDataAddressGeoLocation(
                lat = mappingObject.lat,
                lng = mappingObject.lng,
            )
        }
    }
}

data class UserDataCompany(
    val name: String,
    val catchPhrase: String,
    val bs: String,
){
    companion object {
        fun from(mappingObject: UserDataCompanyDataSourceModel): UserDataCompany {
            return UserDataCompany(
                name = mappingObject.name,
                catchPhrase = mappingObject.catchPhrase,
                bs = mappingObject.bs,
            )
        }
    }
}

data class UserComment(
    val id: Int,
    val title: String,
    val body: String,
){
    companion object {

        fun from(mappingObject: List<UserCommentDataSourceModel>): List<UserComment> {
            return mappingObject.map {
                UserComment(
                    id = it.id,
                    title = it.title,
                    body = it.body,
                )
            }
        }
    }
}
