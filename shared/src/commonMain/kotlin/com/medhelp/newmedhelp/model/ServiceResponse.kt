package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
open class ServiceResponse {
    @SerialName("id_service")
    var id: Int? = null
    @SerialName("id_spec")
    var idSpec: Int? = null
    @SerialName("admission")
    var admission: Int? = null
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
    var max_zapis: Int? = null
    @SerialName("poryadok")
    var poryadok: Int? = null
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

    constructor(id: Int, title: String, value: Int) {
        this.id = id
        this.title = title
        this.value = value.toString()
    }

    constructor(){}

}