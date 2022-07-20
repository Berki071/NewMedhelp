package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BonusesItem {
    @SerialName("data_bonus")
    var date: String? = null

    @SerialName("sym_bonus")
    var value = 0

    @SerialName("statys_bonus")
    var status: String? = null

    constructor() {} // for ios

    constructor(item: BonusesItem) {
        date = item.date
        value = item.value
        status = item.status
    }
}