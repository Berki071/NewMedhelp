package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DateResponse {
    @SerialName("today")
    var today: String? = null

    @SerialName("time")
    var time: String? = null

    @SerialName("week_day")
    var weekDay: String? = null

    @SerialName("last_monday")
    var lastMonday: String? = null
}