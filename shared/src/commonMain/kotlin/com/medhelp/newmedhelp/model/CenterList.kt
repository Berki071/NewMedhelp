package com.medhelp.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CenterList {
    @SerialName("imgError")
    val error = false

    @SerialName("Message")
    val message: String? = null

    @SerialName("response")
    val response: List<CenterResponse> = ArrayList<CenterResponse>()
}