package com.medhelp.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserResponse() : Comparable<UserResponse> {
    @SerialName("id_user") var idUser : Int? = null
    @SerialName("username") var login: String? = null
    @SerialName("id_center") var idCenter : Int? = null
    @SerialName("token") var apiKey: String? = null
    @SerialName("id_filial") var idBranch : Int? = null
    @SerialName("naim_filial") var nameBranch: String? = null

    var surname: String? = null
    var name: String? = null
    var patronymic: String? = null
    var phone: String? = null
    var birthday: String? = null
    var email: String? = null


    override fun compareTo(other: UserResponse): Int {
        return (idUser!! - other.idUser!!) as Int
    }

}