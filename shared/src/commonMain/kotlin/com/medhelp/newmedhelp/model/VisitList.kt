package com.medhelp.newmedhelp.model

import com.medhelp.shared.model.UserResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class VisitList {
    @SerialName("imgError")
    var error : Boolean = false

    @SerialName("message")
    var message : String = ""

    @SerialName("response")
    val response : List<VisitResponse> = ArrayList<VisitResponse>()

}