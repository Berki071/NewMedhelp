package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
open class ServiceResponse {
    @SerialName("id_service")
    var id = 0
    @SerialName("id_spec")
    var idSpec = 0
    @SerialName("admission")
    var admission = 0
    @SerialName("value")
    var value: String? = null
    @SerialName("title")
    var title: String? = null
    @SerialName("komment")
    var hint: String? = null
    @SerialName("zapis")
    var possibilityToEnroll: String? = null
    @SerialName("izbrannoe")
    var favorites: String? = null
    @SerialName("max_zapis")
    var max_zapis = 0
    @SerialName("poryadok")
    var poryadok = 0
    @SerialName("name_spec")
    var name_spec: String? = null

    constructor(item : ServiceResponse) {
        id = item.id
        idSpec = item.idSpec
        admission = item.admission
        value = item.value
        title = item.title
        hint = item.hint
        possibilityToEnroll = item.possibilityToEnroll
        favorites = item.favorites
        max_zapis = item.max_zapis
        poryadok = item.poryadok
        name_spec=item.name_spec
    }

    constructor(title: String, value: Int) {
        this.title = title
        this.value = value.toString()
    }

    constructor(){}

}