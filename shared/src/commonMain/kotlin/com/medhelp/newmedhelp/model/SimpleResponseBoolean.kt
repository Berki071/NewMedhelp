package com.medhelp.newmedhelp.model

import com.medhelp.shared.model.UserResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SimpleResponseBoolean {
    @SerialName("imgError")
    val error = false
    @SerialName("Message")
    val message: String? = null
    @SerialName("response")
    val response: Boolean=false
}