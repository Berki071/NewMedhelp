package com.medhelp.medhelp.data.model._heritable

import com.medhelp.medhelp.utils.TimesUtils
import com.medhelp.newmedhelp.model.BonusesItem

class BonusesItemAndroid : BonusesItem , Comparable<BonusesItemAndroid> {
    constructor(item : BonusesItem) : super(item) {}

    override fun compareTo(other: BonusesItemAndroid): Int {
        val l1= TimesUtils.stringToLong(date!!,TimesUtils.DATE_FORMAT_ddMMyyyy);
        val l2= TimesUtils.stringToLong(other.date!!,TimesUtils.DATE_FORMAT_ddMMyyyy);

        return l1!!.compareTo(l2!!);
    }
}