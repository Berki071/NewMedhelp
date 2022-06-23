package com.medhelp.newmedhelp.model

import android.os.Parcel
import android.os.Parcelable
import com.medhelp.medhelp.utils.TimesUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class VisitResponseAndroid() : VisitResponse(),Comparable<VisitResponseAndroid>, Parcelable {

    constructor(item : VisitResponse) : this() {
        idRecord=item.idRecord
        idServices=item.idServices
        nameServices=item.nameServices
        id_specialty =item.id_specialty
        dateOfReceipt=item.dateOfReceipt
        timeOfReceipt=item.timeOfReceipt
        status=item.status
        call=item.call
        idSotr =item.idSotr
        photoSotr =item.photoSotr
        nameSotr =item.nameSotr
        works =item.works
        idUser =item.idUser
        idBranch =item.idBranch
        nameBranch =item.nameBranch
        cabinet =item.cabinet
        comment =item.comment
        durationService =item.durationService
        price =item.price
        dop =item.dop
        isAddInBasket =item.isAddInBasket
        timeMils =item.timeMils
        userName =item.userName
        executeTheScenario =item.executeTheScenario
        durationSec =item.durationSec
    }

    constructor(parcel: Parcel) : this() {
        idRecord = parcel.readInt()
        idServices = parcel.readInt()
        nameServices = parcel.readString()
        id_specialty = parcel.readInt()
        dateOfReceipt = parcel.readString()
        timeOfReceipt = parcel.readString()
        status = parcel.readString()
        call = parcel.readString()
        idSotr = parcel.readInt()
        photoSotr = parcel.readString()
        nameSotr = parcel.readString()
        works = parcel.readString()
        idUser = parcel.readInt()
        idBranch = parcel.readInt()
        nameBranch = parcel.readString()
        cabinet = parcel.readString()
        comment = parcel.readString()
        durationService = parcel.readInt()
        price = parcel.readInt()
        dop = parcel.readString()
        timeMils = parcel.readLong()
        userName = parcel.readString()
        executeTheScenario = parcel.readString()
        durationSec = parcel.readInt()
    }

    fun getTimeMills() : Long? {
        if (timeMils == 0L)
            return TimesUtils.stringToLong((timeOfReceipt + " " + dateOfReceipt), TimesUtils.DATE_FORMAT_HHmm_ddMMyyyy);
        else
            return timeMils;
    }

    override fun compareTo(other: VisitResponseAndroid): Int {
        val dateCurrent: Long = TimesUtils.stringToLong(dateOfReceipt!!, TimesUtils.DATE_FORMAT_ddMMyyyy) ?: 0
        val dateObj: Long = TimesUtils.stringToLong(other.dateOfReceipt!!, TimesUtils.DATE_FORMAT_ddMMyyyy) ?: 0

        return if (dateCurrent > dateObj) 1 else if (dateCurrent < dateObj) -1 else {
            val timeCurrent: Long = TimesUtils.stringToLong(timeOfReceipt!!, TimesUtils.DATE_FORMAT_HHmm) ?: 0
            val timeObj: Long = TimesUtils.stringToLong(other.timeOfReceipt!!, TimesUtils.DATE_FORMAT_HHmm) ?: 0
            timeCurrent.compareTo(timeObj)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idRecord)
        parcel.writeInt(idServices)
        parcel.writeString(nameServices)
        parcel.writeInt(id_specialty)
        parcel.writeString(dateOfReceipt)
        parcel.writeString(timeOfReceipt)
        parcel.writeString(status)
        parcel.writeString(call)
        parcel.writeInt(idSotr)
        parcel.writeString(photoSotr)
        parcel.writeString(nameSotr)
        parcel.writeString(works)
        parcel.writeInt(idUser)
        parcel.writeInt(idBranch)
        parcel.writeString(nameBranch)
        parcel.writeString(cabinet)
        parcel.writeString(comment)
        parcel.writeInt(durationService)
        parcel.writeInt(price)
        parcel.writeString(dop)
        parcel.writeLong(timeMils)
        parcel.writeString(userName)
        parcel.writeString(executeTheScenario)
        parcel.writeInt(durationSec)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VisitResponseAndroid> {
        override fun createFromParcel(parcel: Parcel): VisitResponseAndroid {
            return VisitResponseAndroid(parcel)
        }

        override fun newArray(size: Int): Array<VisitResponseAndroid?> {
            return arrayOfNulls(size)
        }
    }
}