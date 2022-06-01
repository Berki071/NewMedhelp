package com.medhelp.medhelp.ui.splash

import android.os.Handler
import android.util.Log
import com.androidnetworking.error.ANError
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.medhelp.medhelp.data.model.chat.SimpleResBoolean
import com.medhelp.medhelp.data.network.ProtectionData
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.utils.main.MainUtils
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.newmedhelp.MUtils
import com.medhelp.shared.model.CenterResponse
import com.medhelp.shared.model.SettingsAllBranchHospitalResponse
import com.medhelp.shared.model.UserResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import com.medhelp.medhelp.data.network.NetworkManager as NM
import com.medhelp.shared.network.NetworkManager
import io.reactivex.disposables.CompositeDisposable

class SplashPresenter(var mainView: SplashActivity) {
    var crashlytics = FirebaseCrashlytics.getInstance()

    val prefManager = PreferencesManager(mainView)
    val networkManager = NM(prefManager)
    val networkManager2 = NetworkManager()

    val mainScope = MainScope() //+

    fun testOpenNextActivity(id_kl: String?, id_filial: String?) {
        var username: String? = null
        var password: String? = null
        try {
            username = prefManager.currentLogin
            password = prefManager.currentPassword
        } catch (e: Exception) {
            Timber.tag("my")
                .e(LoggingTree.getMessageForError(e, "SplashPresenter\$testOpenNextActivity "))
        }
        if (username != null && password != null && password != "" && username != "") {
            verifyUser(username, password, true, id_kl, id_filial)
        } else {
            mainView.openLoginActivity()
        }
    }

    private fun verifyUser(
        username: String,
        password: String,
        isRepeatOnErrorConnection: Boolean,
        id_kl: String?,
        id_filial: String?
    ) {
        mainScope.launch {
            kotlin.runCatching {
                networkManager2.doLoginApiCall(
                    ProtectionData().getSignature(
                        mainView
                    )!!, username, password
                )
            }
                .onSuccess {
                    if (it.response[0].login != null) {
                        prefManager.usersLogin = it.response

                        //Fcm для входа аод юзером нотификашки
                        if (id_kl != null && id_filial != null) {
                            for (tmp in it.response) {
                                if (tmp.idUser.toString() == id_kl && tmp.idBranch.toString() == id_filial) {
                                    prefManager.currentUserInfo = tmp
                                    break
                                }
                            }
                        }
                        val dd = prefManager.currentUserInfo

//                        val str = MUtils.objectToString(it.response[0])
//                        Log.wtf("ddefee", str)

                        if (dd == null || dd.apiKey == null) prefManager.currentUserInfo = it.response[0]
                        updateHeaderInfo()
                    } else {
                        prefManager.currentPassword = ""
                        prefManager.usersLogin = null
                        mainView.openLoginActivity()
                    }
                }.onFailure {
                    Timber.tag("my")
                        .e(LoggingTree.getMessageForError(it, "SplashPresenter\$verifyUser "))

                    if (it is ANError) {
                        val anError = it
                        if (anError != null && anError.errorDetail != null && anError.errorDetail.contains(
                                "connectionError"
                            ) && isRepeatOnErrorConnection
                        ) {
                            delayedRestartVerifyUser(username, password, false, id_kl, id_filial)
                        } else crashlytics.log(anError.errorDetail + "//11//" + anError.errorBody)
                    } else {
                        crashlytics.log(it.message!!)
                    }
                    val str = it.message
                    if (str != null && str != "" && str.contains("Failed to connect to")) {
                        mainView.openProfileActivity(prefManager.screenLock)
                    } else {
                        mainView.openLoginActivity()
                    }
                }
        }
    }

    private fun delayedRestartVerifyUser(
        username: String,
        password: String,
        isRepeatOnErrorConnection: Boolean,
        id_kl: String?,
        id_filial: String?
    ) {
        Handler().postDelayed({
            verifyUser(username, password, isRepeatOnErrorConnection, id_kl, id_filial)
        }, 2000)
    }

    private fun updateHeaderInfo() {
        mainScope.launch {
            kotlin.runCatching {
                networkManager2.getCenterApiCall(prefManager.currentUserInfo!!.idCenter.toString())
            }
                .onSuccess {
                    saveCenterInfo(it.response[0])
                }.onFailure {
                    Timber.tag("my")
                        .e(LoggingTree.getMessageForError(it, "SplashPresenter\$updateHeaderInfo "))
                    mainView.openLoginActivity()
                    if (it is ANError) {
                        val anError = it
                        crashlytics.log(anError.errorDetail + "//12//" + anError.errorBody)
                    } else {
                        crashlytics.log(it.message!!)
                    }
                }
        }
    }

    private fun saveCenterInfo(response: CenterResponse) {
        prefManager.centerInfo = response
        // getDataHelper().setCurrentCenterUrl(response.getIpAddress());
        currentUserInfo
    }

    private val currentUserInfo: Unit
        private get() {
            val list = prefManager.usersLogin
            if (list == null || list.size == 0) {
                Timber.tag("my")
                    .e(LoggingTree.getMessageForError(null, "LoginPresenter\$getCenterInfo 0"))
                mainView.hideLoading()
                mainView.showError("Ошибка загрузки информации о пользователе")
            }
            for (tmp in list!!) {
                mainScope.launch {
                    kotlin.runCatching {
                        networkManager2.getCurrentUserInfoInCenter(
                            tmp.idUser!!.toString(),
                            tmp.idBranch!!.toString(),
                            prefManager.currentUserInfo!!.apiKey!!,
                            prefManager.centerInfo!!.db_name!!,
                            prefManager.currentUserInfo!!.idUser.toString(),
                            prefManager.currentUserInfo!!.idBranch.toString()
                        )
                    }
                        .onSuccess {
                            tmp.surname = MUtils.encodeDecodeWord(
                                it.responses[0].surname!!,
                                it.responses[0].keySurname!!
                            )
                            tmp.name = it.responses[0].name
                            tmp.patronymic = it.responses[0].patronymic
                            tmp.phone = MUtils.encodeDecodeWord(
                                it.responses[0].phone!!,
                                it.responses[0].keyPhone!!
                            )
                            tmp.birthday = it.responses[0].birthday
                            tmp.email = it.responses[0].email
                            if (prefManager.currentUserInfo!!.idUser == tmp.idUser)
                                prefManager.currentUserInfo = tmp
                            var boo = true
                            for (tm in list) {
                                if (tm.name == null) {
                                    boo = false
                                    break
                                }
                            }
                            if (boo) {
                                prefManager.usersLogin = list
                                firebaseId
                            }
                        }.onFailure {
                            Timber.tag("my").e(
                                LoggingTree.getMessageForError(
                                    it,
                                    "SplashPresenter\$getCurrentUserInfo "
                                )
                            )
                            mainView.openLoginActivity()
                            if (it is ANError) {
                                val anError = it
                                crashlytics.log(anError.errorDetail + "//13//" + anError.errorBody)
                            } else {
                                crashlytics.log(it.message!!)
                            }
                        }
                }
            }
        }
    val firebaseId: Unit
        get() {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful || task.result == null) {
                        Timber.tag("my")
                            .e("SplashActivity\$getFirebaseId getInstanceId failed " + task.exception)
                        allHospitalBranch
                        return@OnCompleteListener
                    }
                    val token = task.result
                    sendFcmToken(token)
                })
        }

    private var countFcmSend=0
    private fun sendFcmToken(token: String) {
        val list = prefManager.usersLogin
        if (list == null || list.size == 0) {
            allHospitalBranch
            return
        }

        countFcmSend = list.size

        for (i in list) {
            mainScope.launch {
                kotlin.runCatching {
                    networkManager2.sendFcmId(i.idUser.toString(), i.idBranch.toString(), token, prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
                }
                    .onSuccess {
                        countFcmSend--
                        if(countFcmSend<=0)
                            allHospitalBranch

                    }.onFailure {
                        if (it is ANError) {
                            val anError = it
                            crashlytics.log(anError.errorDetail + "//7//" + anError.errorBody)
                        } else {
                            crashlytics.log(it.message!!)
                        }
                        Timber.tag("my").e(LoggingTree.getMessageForError(it, "LoginPresenter\$restorePass "))
                        mainView.hideLoading()
                        mainView.showError("Ошибка восстановления пароля")
                    }
            }
        }
    }

    private val allHospitalBranch: Unit
        private get() {

            mainScope.launch {
                kotlin.runCatching {
                    networkManager2.getAllHospitalBranch(prefManager.currentUserInfo!!.idCenter.toString())
                }
                    .onSuccess {
                        processingYandexKey(it.response)
                        mainView.openProfileActivity(prefManager.screenLock)
                    }.onFailure {
                        Timber.tag("my").e(LoggingTree.getMessageForError(it, "SplashPresenter\$getAllHospitalBranch"))
                        mainView.openProfileActivity(prefManager.screenLock)
                    }
            }
        }

    private fun processingYandexKey(resp: List<SettingsAllBranchHospitalResponse>) {
        var keyExist = false
        var allExist = true
        for (tmp in resp) {
            if (tmp.idShop == null || tmp.idShop == "") {
                allExist = false
            } else {
                keyExist = true
            }
        }
        if (allExist) {
            prefManager.yandexStoreIsWorks = true
        } else {
            prefManager.yandexStoreIsWorks = false
            if (keyExist) {
                Timber.tag("my").e(LoggingTree.getMessageForError(null, "Не у всех филиалов прописан Yandex IdShop"))
            }
        }
    }
}