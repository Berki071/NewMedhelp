package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class SpecialtyList {
    @SerialName("imgError")
    var error = false

    @SerialName("message")
    var message: String? = null

    @SerialName("response")
    var spec: List<CategoryResponse> = ArrayList<CategoryResponse>()
}