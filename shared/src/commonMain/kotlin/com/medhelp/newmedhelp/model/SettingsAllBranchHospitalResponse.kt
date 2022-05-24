package com.medhelp.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SettingsAllBranchHospitalResponse  : Comparable<SettingsAllBranchHospitalResponse>{
    @SerialName("id_filial")
    val idBranch = 0

    @SerialName("naim_filial")
    val nameBranch: String? = null

    @SerialName("shopid")
    val idShop: String? = null

    @SerialName("secretkey")
    val keyShope: String? = null

    @SerialName("clientkey")
    val keyAppYandex: String? = null

    var isFavorite = false

    override fun compareTo(other: SettingsAllBranchHospitalResponse): Int {
        var res1: Int = isFavorite.compareTo(other.isFavorite)
        res1 *= -1

        return if (res1 != 0) res1 else nameBranch!!.compareTo(other.nameBranch!!)
    }
}