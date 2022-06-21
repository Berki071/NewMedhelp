package com.medhelp.medhelp.utils

import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object TimesUtils {
    const val DATE_FORMAT_HHmmss_ddMMyyyy = "HH:mm:ss dd.MM.yyyy"
    const val DATE_FORMAT_HHmm = "HH:mm"
    const val DATE_FORMAT_HHmmss = "HH:mm:ss"
    const val DATE_FORMAT_HHmm_ddMMyyyy = "HH:mm dd.MM.yyyy"
    const val DATE_FORMAT_ddMMyyyy = "dd.MM.yyyy"
    const val DATE_FORMAT_dd_MMMM_yyyy = "dd MMMM yyyy"
    const val DATE_FORMAT_ddMMyy = "dd.MM.yy"

    fun UtcLongToStrLocal(date: Long): String {
        val loc = TimeZone.getDefault()
        val formatter = SimpleDateFormat(DATE_FORMAT_HHmmss_ddMMyyyy)
        val lDate = Date(date + loc.rawOffset)
        return formatter.format(lDate)
    }

    fun localLongToUtcLong(local: Long): Long {
        val loc = TimeZone.getDefault()
        val lData = Date(local - loc.rawOffset)
        return lData.time
    }

    @JvmStatic
    fun stringToLong(date: String, format: String): Long? {
        val sdf = SimpleDateFormat(format)
        try {
            val data = sdf.parse(date)
            return data.time
        } catch (e: ParseException) {
            Timber.tag("my").e(getMessageForError(e, "TimesUtils stringToLong"))
        }
        return null
    }

    fun getNewFormatString(data: String?, oldFormat: String?, newFormat: String?): String {
        val format: DateFormat = SimpleDateFormat(oldFormat)
        var date: Date? = null
        try {
            date = format.parse(data)
        } catch (e: ParseException) {
            Timber.tag("my").e(getMessageForError(e, "getNewFormatString"))
        }
        assert(date != null)
        val dataLong = date!!.time
        val dateFormat = SimpleDateFormat(newFormat)
        return dateFormat.format(Date(dataLong))
    }

    fun longToNewFormatLong(date: Long, format: String?): Long {
        val dateFormat: DateFormat = SimpleDateFormat(format)
        var d = Date(date)
        val newD = dateFormat.format(d)
        try {
            d = dateFormat.parse(newD)
            return d.time
        } catch (e: ParseException) {
            Timber.tag("my").e(getMessageForError(e, "            Timber.e(e,);\n"))
        }
        return 0
    }

    @JvmStatic
    fun longToString(date: Long, format: String?): String {
        val df: DateFormat = SimpleDateFormat(format)
        val d = Date(date)
        return df.format(d)
    }

    @JvmStatic
    fun getMillisFromDateString(time: String?): Long {
        val f = SimpleDateFormat("HH:mm:ss dd.MM.yyyy", Locale.getDefault())
        val f2 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return try {
            val d = f.parse(time)
            val s = f2.format(d)
            val d2 = f2.parse(s)
            d2.time
        } catch (e: ParseException) {
            Timber.tag("my").e(getMessageForError(e, "getMillisFromDateString"))
            0L
        }
    }

    @JvmStatic
    fun stringToDate(data: String?): Date? {
        val format: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        try {
            return format.parse(data)
        } catch (e: ParseException) {
            Timber.tag("my").e(getMessageForError(e, "stringToDate"))
        }
        return null
    }

    @JvmStatic
    fun dateToString(data: Date?, newFormat: String?): String {
        val format: DateFormat = SimpleDateFormat(newFormat, Locale.getDefault())
        return format.format(data)
    }

    fun getCurrentDate(format: String?): String {
        val mill = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(mill)
        return dateFormat.format(date)
    }

    @JvmStatic
    fun compareWithCurrentTimeByDate(data2: String): Int {
        val mill = System.currentTimeMillis()
        val dateClear = longToNewFormatLong(mill, DATE_FORMAT_ddMMyyyy)
        val date2Clear = stringToLong(data2, DATE_FORMAT_ddMMyyyy)
        return if (dateClear > date2Clear!!) -1 else if (dateClear < date2Clear) 1 else 0
    }
}