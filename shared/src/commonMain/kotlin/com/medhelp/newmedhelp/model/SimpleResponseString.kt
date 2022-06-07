package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SimpleResponseString {
    @SerialName("imgError")
    val error = false

    @SerialName("message")
    val message: String? = null

    @SerialName("response")
    val response: String? = null
}