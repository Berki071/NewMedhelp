package com.medhelp.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CurrentUserInfoResponse {
    @SerialName("fam")
    val surname: String? = null

    @SerialName("nam")
    val name: String? = null

    @SerialName("otch")
    val patronymic: String? = null

    @SerialName("dr")
    val birthday: String? = null

    @SerialName("tel")
    val phone: String? = null

    @SerialName("email")
    val email: String? = null

    @SerialName("kf")
    val keySurname: String? = null

    @SerialName("kt")
    val keyPhone: String? = null
}