package com.medhelp.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
open class CenterResponse {
    @SerialName("id")
    var id = 0
    @SerialName("id_center")
    var idCenter = 0
    @SerialName("id_filial")
    var idFilial = 0
    @SerialName("city")
    var city: String? = null
    @SerialName("time_zone")
    var timeZone = 0
    @SerialName("title")
    var title: String? = null
    @SerialName("info")
    var info: String? = null
    @SerialName("logo")
    var logo: String? = null
    @SerialName("site")
    var site: String? = null
    @SerialName("phone")
    var phone: String? = null
    @SerialName("address")
    var address: String? = null
    @SerialName("time_otkaz") //отказ от приема
    var timeForDenial = 0
    @SerialName("time_podtvergd") //подтвердить
    var timeForConfirm = 0
    @SerialName("komment_zapis")
    var comment_to_record: String? = null
    @SerialName("max_zapis")
    var max_records = 0
    @SerialName("db_name")
    var db_name: String? = null

    @SerialName("button_zapis")
    var button_zapis: Int = 0
    @SerialName("button_doctors")
    var button_doctors: Int = 0
    @SerialName("button_price_ysl")
    var button_price_ysl: Int = 0
    @SerialName("button_price_anal")
    var button_price_anal: Int = 0
    @SerialName("button_result_zakl")
    var button_result_zakl: Int = 0
    @SerialName("button_sdan_anal")
    var button_sdan_anal: Int = 0
    @SerialName("button_result_anal")
    var button_result_anal: Int = 0
    @SerialName("button_nalog")
    var button_nalog: Int = 0
    @SerialName("button_fin")
    var button_fin: Int = 0
}