package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class AnalisePriceResponse {
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

    constructor(title: String, price: Int, time: Int) {   //for ios
        this.title = title
        this.price = price
        this.time = time
    }


}