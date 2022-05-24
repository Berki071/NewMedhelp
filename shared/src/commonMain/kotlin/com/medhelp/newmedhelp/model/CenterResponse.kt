package com.medhelp.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class CenterResponse {
    @SerialName("id")
    val id = 0
    @SerialName("id_center")
    val idCenter = 0
    @SerialName("id_filial")
    val idFilial = 0
    @SerialName("city")
    val city: String? = null
    @SerialName("time_zone")
    val timeZone = 0
    @SerialName("title")
    val title: String? = null
    @SerialName("info")
    val info: String? = null
    @SerialName("logo")
    val logo: String? = null
    @SerialName("site")
    val site: String? = null
    @SerialName("phone")
    val phone: String? = null
    @SerialName("address")
    val address: String? = null
    @SerialName("time_otkaz") //отказ от приема
    val timeForDenial = 0
    @SerialName("time_podtvergd") //подтвердить
    val timeForConfirm = 0
    @SerialName("komment_zapis")
    val comment_to_record: String? = null
    @SerialName("max_zapis")
    val max_records = 0
    @SerialName("db_name")
    val db_name: String? = null

    @SerialName("button_zapis")
    val button_zapis: Int = 0
    @SerialName("button_doctors")
    val button_doctors: Int = 0
    @SerialName("button_price_ysl")
    val button_price_ysl: Int = 0
    @SerialName("button_price_anal")
    val button_price_anal: Int = 0
    @SerialName("button_result_zakl")
    val button_result_zakl: Int = 0
    @SerialName("button_sdan_anal")
    val button_sdan_anal: Int = 0
    @SerialName("button_result_anal")
    val button_result_anal: Int = 0
    @SerialName("button_nalog")
    val button_nalog: Int = 0
    @SerialName("button_fin")
    val button_fin: Int = 0
}