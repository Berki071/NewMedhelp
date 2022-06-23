package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class AllDoctorsList {
    @SerialName("imgError")
    var error = false

    @SerialName("message")
    var message: String? = null

    @SerialName("response")
    var mResponses: List<AllDoctorsResponse> = ArrayList<AllDoctorsResponse>()
}