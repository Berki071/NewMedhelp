package com.medhelp.newmedhelp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class VisitResponse()  {
    @SerialName("id_zapisi")
    var idRecord = 0
    @SerialName("idysl")
    var idServices = 0
    @SerialName("naim_ysl")
    var nameServices: String? = null
    @SerialName("id_specialty")
    var id_specialty = 0
    @SerialName("adm_date")
    var dateOfReceipt: String? = null
    @SerialName("adm_time")
    var timeOfReceipt: String? = null
    @SerialName("status")
    var status: String? = null
    @SerialName("call")
    var call: String? = null
    @SerialName("idsotr")
    var idSotr = 0
    @SerialName("image_url")
    var photoSotr: String? = null
    @SerialName("full_name")
    var nameSotr: String? = null
    @SerialName("rabotaet")
    var works: String? = null
    @SerialName("id_kl")
    var idUser = 0
    @SerialName("id_filial")
    var idBranch = 0
    @SerialName("naim_filial")
    var nameBranch: String? = null
    @SerialName("kabinet")
    var cabinet: String? = null
    @SerialName("komment")
    var comment: String? = null
    @SerialName("dlit")
    var durationService = 0
    @SerialName("price")
    var price = 0
    @SerialName("dop")
    var dop: String? = null

    var isAddInBasket = false
    var timeMils: Long = 0
    var userName: String? = null
    var executeTheScenario: String? = null
    var durationSec = 0
}