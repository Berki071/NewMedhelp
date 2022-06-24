package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnalisePriceResponse {
    @SerialName("gryppa")
    var group: String? = null

    @SerialName("analiz")
    var title: String? = null

    @SerialName("cena")
    var price = 0

    @SerialName("srok")
    var time = 0

    constructor(item : AnalisePriceResponse){    //for ios
        group = item.group
        title = item.title
        price = item.price
        time = item.time
    }
}