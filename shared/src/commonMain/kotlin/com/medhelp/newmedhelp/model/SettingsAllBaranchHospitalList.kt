package com.medhelp.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SettingsAllBaranchHospitalList {
    @SerialName("imgError")
    val error = false

    @SerialName("message")
    val message: String? = null

    @SerialName("response")
    val response: List<SettingsAllBranchHospitalResponse> = ArrayList()
}