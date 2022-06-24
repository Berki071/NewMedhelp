package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnalisePriceList {
    @SerialName("imgError")
    var error = false

    @SerialName("Message")
    var message: String? = null

    @SerialName("response")
    var response: List<AnalisePriceResponse> = ArrayList<AnalisePriceResponse>()
}