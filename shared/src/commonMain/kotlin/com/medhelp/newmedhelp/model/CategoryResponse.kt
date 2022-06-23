package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class CategoryResponse {
    @SerialName("id_spec")
    var id = -1

    @SerialName("title")
    var title: String? = null

    constructor(title: String?) {
        this.title = title
    }
}