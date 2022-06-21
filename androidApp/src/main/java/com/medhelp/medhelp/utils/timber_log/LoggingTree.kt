package com.medhelp.medhelp.utils.timber_log

import android.content.Context
import android.content.pm.PackageManager
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.shared.network.NetworkManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber


class LoggingTree(context: Context) : Timber.DebugTree() {
    companion object {
        //Timber.e - for error (priority 6)
        //Timber.i - for onCreate Activity (priority 4)
        //Timber.v - verbose information about actions (priority 2)
        //Timber.d - ALERT_TYPE (priority 3)
        const val VERBOSE_INFORMATION = "VERBOSE_INFORMATION"
        const val CREATE_ACTIVITY = "CREATE_ACTIVITY"
        const val ERROR = "ERROR"
        const val ERROR_PAYMENT = "ERROR_PAYMENT"
        const val DEFAULT_ERROR_TYPE = "DEFAULT_ERROR_TYPE"
        const val ALERT_TYPE = "ALERT_TYPE"
        @JvmStatic
        fun getMessageForError(t: Throwable?, message: String): String {
            var err: String? = ""
            if (t != null) {
//                if (t is ANError) {
//                    val anError = t
//                    err += "ANError ErrorDetail: "
//                    err += anError.errorDetail
//                    err += "\n"
//                    err += "ANError ErrorBody: "
//                    err += anError.errorBody
//                    err += "\n"
//                }
                err += "Throwable message: "
                err += t.message
            }
            return """
                   $message
                   $err
                   """.trimIndent()
        }
    }

    private val networkManager: NetworkManager
    private var versionCode = "0"
    var pm: PreferencesManager
    val mainScope = MainScope()

    init {
        pm = PreferencesManager(context)
        networkManager = NetworkManager()
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val verCode = pInfo.versionCode
            versionCode = verCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    protected override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (tag == null || tag != "my") return
        when (priority) {
            2 -> sendLogToServer(VERBOSE_INFORMATION, message)
            3 -> sendLogToServer(ALERT_TYPE, message)
            4 -> sendLogToServer(CREATE_ACTIVITY, message)
            6 -> sendLogToServer(ERROR, message)
            else -> sendLogToServer(DEFAULT_ERROR_TYPE, message)
        }
    }

    private fun sendLogToServer(type: String, message: String) {
        var type = type
        var message = message
        if (type == ERROR && message.contains("ShoppingBasket")) {
            type = ERROR_PAYMENT
        }
        message = message.trim { it <= ' ' }

        var idUSer = pm.currentUserInfo!!.idUser ?: 0
        var idBranch = pm.currentUserInfo!!.idBranch ?: 0
        var idCenter = pm.currentUserInfo!!.idCenter ?: 0

        mainScope.launch {
            kotlin.runCatching {
                networkManager.sendLogToServer(type, message, versionCode, idUSer.toString(), idBranch.toString(), idCenter.toString())
            }
                .onSuccess {}
                .onFailure {}
        }
    }

}