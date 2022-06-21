package com.medhelp.newmedhelp.model

import com.medhelp.shared.model.UserResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SimpleResponseBoolean {
    @SerialName("error")
    var error = false
    @SerialName("message")
    var message: String? = null
    @SerialName("response")
    var response: Boolean=false
}