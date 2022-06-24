package com.medhelp.medhelp.data.model._heritable

import android.os.Parcelable
import android.os.Parcel
import com.medhelp.medhelp.data.model._heritable.ServiceResponseAndroid
import com.medhelp.newmedhelp.model.ServiceResponse
import com.medhelp.newmedhelp.model.VisitResponse
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName


class ServiceResponseAndroid() : ServiceResponse(), Comparable<ServiceResponseAndroid> {

    constructor(item : ServiceResponse) : this() {
        id = item.id
        idSpec = item.idSpec
        admission = item.admission
        value = item.value
        title = item.title
        hint = item.hint
        possibilityToEnroll = item.possibilityToEnroll
        max_zapis = item.max_zapis
        poryadok = item.poryadok
        favorites = item.favorites
    }

    override fun compareTo(o: ServiceResponseAndroid): Int {
        return title!!.compareTo(o.title!!)
    }

}