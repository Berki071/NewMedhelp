package com.medhelp.medhelp.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.medhelp.medhelp.data.model.DataPaymentForRealm
import com.medhelp.shared.model.CenterResponse
import com.medhelp.shared.model.UserResponse
import java.util.*

const val PREF_NAME = "medhelp_mhchat_pref"

class PreferencesManager(context: Context) {
    companion object {
        private const val PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME"
        private const val PREF_KEY_CURRENT_PASSWORD = "PREF_KEY_CURRENT_PASSWORD"
        private const val PREF_KEY_START_MODE = "PREF_KEY_START_MODE"
        private const val PREF_KEY_TOOLTIP_SCHEDULE = "PREF_KEY_TOOLTIP_SCHEDULE"
        private const val PREF_KEY_TOOLTIP_ANALISE_LIST_TRUE = "PREF_KEY_TOOLTIP_ANALISE_LIST_TRUE"
        private const val PREF_KEY_TOOLTIP_ANALISE_LIST_FALSE =
            "PREF_KEY_TOOLTIP_ANALISE_LIST_FALSE"
        private const val PREF_KEY_TOOLTIP_ANALISE_RESULT_LOAD =
            "PREF_KEY_TOOLTIP_ANALISE_RESULT_LOAD"
        private const val PREF_KEY_TOOLTIP_ANALISE_RESULT_DELETE =
            "PREF_KEY_TOOLTIP_ANALISE_RESULT_DELETE"
        private const val PREF_KEY_TOOLTIP_SEARCH_ITEM = "PREF_KEY_TOOLTIP_SEARCH_ITEM"
        private const val PREF_KEY_TOOLTIP_START_ROOM = "PREF_KEY_TOOLTIP_START_ROOM"
        private const val PREF_KEY_TOOLTIP_PROFILE_CENTER = "PREF_KEY_TOOLTIP_PROFILE_CENTER"
        private const val PREF_KEY_TOOLTIP_SEARCH_TAB = "PREF_KEY_TOOLTIP_SEARCH_TAB"
        private const val PREF_KEY_TOOLTIP_SEARCH_LOUPE = "PREF_KEY_TOOLTIP_SEARCG_LOUPE"
        private const val PREF_KEY_TOOLTIP_TAX = "PREF_KEY_TOOLTIP_TAX"
        private const val PREF_KEY_RATING_APP = "PREF_KEY_RATING_APP"
        private const val PREF_KEY_SCREEN_LOCK = "PREF_KEY_SCREEN_LOCK"
        private const val PREF_KEY_CENTER_INFO = "PREF_KEY_CENTER_INFO"
        private const val PREF_KEY_CURRENT_USER_INFO = "PREF_KEY_CURRENT_USER_INFO"
        private const val PREF_KEY_USERS_LOGIN = "PREF_KEY_USERS_LOGIN"
        private const val PREF_KEY_YANDEX_STORE = "PREF_KEY_YANDEX_STORE"
        private const val PREF_KEY_SHOW_ALERT_QUESTION_AT_EXIT = "PREF_KEY_SHOW_ALERT_QUESTION_AT_EXIT"
        private const val PREF_KEY_REQUEST_AUTO_START = "PREF_KEY_REQUEST_AUTO_START"
        private const val PREF_KEY_PAY_DATA = "PREF_KEY_PAY_DATA"
    }

    private var preferences: SharedPreferences

    init {
        val prefName = PREF_NAME
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    var notShowAlertQuestionAtExit: Boolean
        get() = preferences.getBoolean(PREF_KEY_SHOW_ALERT_QUESTION_AT_EXIT, false)
        set(boo) {
            preferences.edit().putBoolean(PREF_KEY_SHOW_ALERT_QUESTION_AT_EXIT, boo).apply()
        }
    val showTooltipTax: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_TAX, true)

    fun setShowTooltipTax() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_TAX, false).apply()
    }

    val showTooltipSearchLoupe: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_SEARCH_LOUPE, true)

    fun setShowTooltipSearchLoupe() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_SEARCH_LOUPE, false).apply()
    }

    var usersLogin: List<UserResponse>?
        get() {
            val tempList = preferences.getStringSet(PREF_KEY_USERS_LOGIN, HashSet())
            if (tempList == null || tempList.toString() == "") return null
            val rrr: MutableList<UserResponse> = ArrayList()
            for (tmp in tempList) {
                rrr.add(Gson().fromJson(tmp, UserResponse::class.java))
            }
            Collections.sort(rrr)
            return rrr
        }
        set(list) {
            val tempList: MutableSet<String> = HashSet()
            if (list == null) {
                preferences.edit().putStringSet(PREF_KEY_USERS_LOGIN, null).apply()
            } else {
                for (tmp in list) {
                    tempList.add(Gson().toJson(tmp))
                }
                preferences.edit().putStringSet(PREF_KEY_USERS_LOGIN, tempList).apply()
            }
        }
    val showTooltipSearchTab: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_SEARCH_TAB, true)

    fun setShowTooltipSearchTab() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_SEARCH_TAB, false).apply()
    }

    var currentRatingApp: Float
        get() = preferences.getFloat(PREF_KEY_RATING_APP, 0f)
        set(userId) {
            preferences.edit().putFloat(PREF_KEY_RATING_APP, userId).apply()
        }
    var centerInfo: CenterResponse?
        get() {
            val tmp = preferences.getString(PREF_KEY_CENTER_INFO, "")
            if (tmp == "") return null
            val gson = Gson()
            return gson.fromJson(tmp, CenterResponse::class.java)
        }
        set(info) {
            val gson = Gson()
            val json = gson.toJson(info)
            preferences.edit().putString(PREF_KEY_CENTER_INFO, json).apply()
        }
    var currentUserInfo: UserResponse?
        get() {
            val tmp = preferences.getString(PREF_KEY_CURRENT_USER_INFO, "")
            if (tmp == null || tmp == "") return null
            val gson = Gson()
            return gson.fromJson(tmp, UserResponse::class.java)
        }
        set(info) {
            val gson = Gson()
            val json = gson.toJson(info)
            preferences.edit().putString(PREF_KEY_CURRENT_USER_INFO, json).apply()
        }
    val showTooltipProfile: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_PROFILE_CENTER, true)

    fun setShowTooltipProfile() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_PROFILE_CENTER, false).apply()
    }

    val showTooltipStartRoom: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_START_ROOM, true)

    fun setShowTooltipStartRoom() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_START_ROOM, false).apply()
    }

    var screenLock: Boolean
        get() = preferences.getBoolean(PREF_KEY_SCREEN_LOCK, false)
        set(boo) {
            preferences.edit().putBoolean(PREF_KEY_SCREEN_LOCK, boo).apply()
        }
    val showTooltipSchedule: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_SCHEDULE, true)

    fun setShowTooltipSchedule() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_SCHEDULE, false).apply()
    }

    val showTooltipAnaliseListTrue: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_ANALISE_LIST_TRUE, true)

    fun setShowTooltipAnaliseListTrue() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_ANALISE_LIST_TRUE, false).apply()
    }

    //return true;
    val showTooltipAnaliseListFalse: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_ANALISE_LIST_FALSE, true)

    //return true;
    fun setShowTooltipAnaliseListFalse() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_ANALISE_LIST_FALSE, false).apply()
    }

    val showTooltipAnaliseResultLoad: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_ANALISE_RESULT_LOAD, true)

    fun setShowTooltipAnaliseResultLoad() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_ANALISE_RESULT_LOAD, false).apply()
    }

    // return true;
    val showTooltipAnaliseResultDelete: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_ANALISE_RESULT_DELETE, true)

    // return true;
    fun setShowTooltipAnaliseResultDelete() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_ANALISE_RESULT_DELETE, false).apply()
    }

    //return true;
    val showTooltipASearchItem: Boolean
        get() = preferences.getBoolean(PREF_KEY_TOOLTIP_SEARCH_ITEM, true)

    //return true;
    fun setShowTooltipASearchItem() {
        preferences.edit().putBoolean(PREF_KEY_TOOLTIP_SEARCH_ITEM, false).apply()
    }

    var currentLogin: String?
        get() = preferences.getString(PREF_KEY_CURRENT_USER_NAME, null)
        set(userName) {
            preferences.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply()
        }
    var currentPassword: String?
        get() = preferences.getString(PREF_KEY_CURRENT_PASSWORD, null)
        set(password) {
            preferences.edit().putString(PREF_KEY_CURRENT_PASSWORD, password).apply()
        }

    fun deleteCurrentPassword() {
        preferences.edit().remove(PREF_KEY_CURRENT_PASSWORD).apply()
    }

    val isStartMode: Boolean
        get() = preferences.getBoolean(PREF_KEY_START_MODE, true)

    fun isStartMode(mode: Boolean) {
        preferences.edit().putBoolean(PREF_KEY_START_MODE, mode).apply()
    }

    var yandexStoreIsWorks: Boolean
        get() = preferences.getBoolean(PREF_KEY_YANDEX_STORE, false)
        set(boo) {
            preferences.edit().putBoolean(PREF_KEY_YANDEX_STORE, boo).apply()
        }

    //false если еще не показывалось
    val requestAutoStart: Boolean
        get() = preferences.getBoolean(
            PREF_KEY_REQUEST_AUTO_START,
            false
        ) //false если еще не показывалось

    fun setRequestAutoStart() {
        preferences.edit().putBoolean(PREF_KEY_REQUEST_AUTO_START, true).apply()
    }


    fun savePaymentData(data: DataPaymentForRealm){
        val gson=Gson()
        val strItem : String= gson.toJson(data)

        var allLogItems=getAllPaymentDataString()
        if(allLogItems==null){
            var newSet = mutableSetOf(strItem)
            preferences!!.edit().putStringSet(PREF_KEY_PAY_DATA, newSet).apply()
        }else{
            val myTreeSet = TreeSet(allLogItems)
            myTreeSet.add(strItem)
            preferences!!.edit().putStringSet(PREF_KEY_PAY_DATA, myTreeSet).apply()
        }
    }
    fun getAllPaymentDataString(): TreeSet<String>? {
        val res=preferences.getStringSet(PREF_KEY_PAY_DATA, null)
        return if(res == null)
            null
        else
            TreeSet(preferences.getStringSet(PREF_KEY_PAY_DATA, null))
    }
    fun deletePaymentData(item : DataPaymentForRealm){
        val gson=Gson()
        val strItem : String= gson.toJson(item)

        var allLogItems=getAllPaymentDataString()

        if(allLogItems!=null) {
            val inSet: MutableSet<String> = HashSet<String>(allLogItems)
            inSet.remove(strItem)
            preferences.edit().putStringSet(PREF_KEY_PAY_DATA, inSet).apply()
        }
    }
    fun getAllPaymentData(): MutableList<DataPaymentForRealm>? {
        val res=getAllPaymentDataString()
        val list = mutableListOf<DataPaymentForRealm>()

        if(res==null)
            return list

        val gson=Gson()
        for(i in res){
            val item : DataPaymentForRealm = gson.fromJson(i, DataPaymentForRealm::class.java)
            list.add(item)
        }

        return list
    }
}