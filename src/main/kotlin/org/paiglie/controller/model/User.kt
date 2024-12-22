package org.paiglie.controller.model

import org.paiglie.service.model.User as UserServiceModel
import org.paiglie.service.model.UserData as UserDataServiceModel
import org.paiglie.service.model.UserComment as UserCommentServiceModel
import org.paiglie.service.model.UserDataAddress as UserDataAddressServiceModel
import org.paiglie.service.model.UserDataAddressGeoLocation as UserDataAddressGeoLocationServiceModel
import org.paiglie.service.model.UserDataCompany as UserDataCompanyServiceModel

data class User(
    val userId: Int,
    val data: UserData,
    val comments: List<UserComment>
){
    companion object {
        fun from(mappingObject: UserServiceModel): User{
            return User(
                mappingObject.userId,
                data = UserData.from(mappingObject.data),
                comments = UserComment.from(mappingObject.comments)
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
        fun from(mappingObject: UserDataServiceModel): UserData {
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
        fun from(mappingObject: UserDataAddressServiceModel): UserDataAddress{
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
        fun from(mappingObject: UserDataAddressGeoLocationServiceModel): UserDataAddressGeoLocation {
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
        fun from(mappingObject: UserDataCompanyServiceModel): UserDataCompany {
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

        fun from(mappingObject: List<UserCommentServiceModel>): List<UserComment> {
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
