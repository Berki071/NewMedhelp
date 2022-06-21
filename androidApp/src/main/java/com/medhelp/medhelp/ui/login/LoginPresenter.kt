package com.medhelp.medhelp.ui.login

import com.androidnetworking.error.ANError
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.network.ProtectionData
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.utils.main.NetworkUtils
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.newmedhelp.MUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import com.medhelp.medhelp.data.network.NetworkManager as NM
import com.medhelp.shared.network.NetworkManager

class LoginPresenter(var mainView: LoginActivity) {
    var crashlytics = FirebaseCrashlytics.getInstance()

    val prefManager = PreferencesManager(mainView)
    val networkManager = NM(prefManager)
    val networkManager2 = NetworkManager()

    val mainScope = MainScope() //+

    fun onLoginClick(username: String?, password: String?) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            mainView.showError(R.string.empty_data)
            return
        }
        if (username.length != 10) {
            mainView.showError(R.string.invalid_data)
            return
        }
        if (NetworkUtils.isNetworkConnected(mainView)) {
            verifyUser(username, password)
        } else {
            mainView.showError(R.string.connection_error)
        }
    }

    private fun verifyUser(username: String, password: String) {
        mainView.showLoading()

        mainScope.launch {
            kotlin.runCatching { networkManager2.doLoginApiCall(ProtectionData().getSignature(mainView)!!, username, password) }
                .onSuccess {
                    if (it.response[0].apiKey != null) {
                        prefManager.usersLogin = it.response

                        //List<UserResponse> ll=getDataHelper().getUsersLogin();
                        if (mainView.isNeedSave) {
                            savePrivateData(username, password)
                        } else {
                            cleanPassword()
                        }

                        //if (getDataHelper().getCurrentUserInfo() == null || getDataHelper().getCurrentUserInfo().getApiKey() == null)
                        prefManager.currentUserInfo = it.response[0]
                        centerInfo
                    } else {
                        mainView.showError(R.string.err_authorize)
                        mainView.hideLoading()
                    }
            }.onFailure {
                    var str: String? = null
                    if (it is ANError) {
                        val anError = it
                        str = anError.errorBody
                        crashlytics.log(anError.errorDetail + "//1//" + anError.errorBody)
                        if (str != null && str.contains("vasdvasdasdvя")) {
                            val dd = ProtectionData().getSignature(mainView)
                            Timber.tag("my").e(
                                LoggingTree.getMessageForError(
                                    Throwable(
                                        "Несовпадение hashkey " + dd
                                    ), "LoginPresenter\$verifyUser"
                                )
                            )
                            //   Log.wtf("wtf", pd.getSignature(view.getContext()));
                        } else {
                            Timber.tag("my").e(
                                LoggingTree.getMessageForError(
                                    it,
                                    "LoginPresenter\$verifyUser "
                                )
                            )
                        }
                    } else {
                        Timber.tag("my").e(
                            LoggingTree.getMessageForError(
                                it,
                                "LoginPresenter\$verifyUser "
                            )
                        )
                        crashlytics.log(it.message!!)
                    }
                    val str11 = it.message
                    if (str11 != null && str != null && str.contains("Failed to connect to")) {
                        mainView.showError("Failed to connect")
                    } else {
                        mainView.showError(R.string.api_default_error)
                    }
                    if (mainView == null) {
                        return@onFailure
                    }
                    mainView.hideLoading()
            }
        }
    }

    private fun cleanPassword() {
        try {
            prefManager.deleteCurrentPassword()
        } catch (e: Exception) {
            Timber.tag("my").e(LoggingTree.getMessageForError(e, "LoginPresenter\$cleanPassword "))
        }
    }

    private fun savePrivateData(username: String, password: String) {
        try {
            prefManager.currentPassword = password
            prefManager.currentLogin = username
        } catch (e: Exception) {
            Timber.tag("my")
                .e(LoggingTree.getMessageForError(e, "LoginPresenter\$savePrivateData "))
        }
    }

    // getDataHelper().setCurrentCenterUrl(centerResponses.getResponse().get(0).getIpAddress());
    private val centerInfo: Unit
        private get() {
            mainView.showLoading()

            mainScope.launch {
                kotlin.runCatching {
                    networkManager2.getCenterApiCall(prefManager.currentUserInfo!!.idCenter.toString())
                }
                    .onSuccess {
                        prefManager.centerInfo = it.response[0]
                        allHospitalBranch
                    }.onFailure {
                        if (it is ANError) {
                            val anError = it
                            crashlytics.log(anError.errorDetail + "//2//" + anError.errorBody)
                        } else {
                            crashlytics.log(it.message!!)
                        }
                        Timber.tag("my").e(LoggingTree.getMessageForError(it, "LoginPresenter/getCenterInfo"))
                        mainView.hideLoading()
                        mainView.showError("Ошибка загрузки информации о центре")
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
                        var keyExist = false
                        var allExist = true
                        for (tmp in it.response) {
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
                                Timber.tag("my").e(
                                    LoggingTree.getMessageForError(
                                        null,
                                        "Не у всех филиалов прописан Yandex IdShop"
                                    )
                                )
                            }
                        }
                        currentUserInfo
                    }.onFailure {
                        if (it is ANError) {
                            val anError = it
                            crashlytics.log(anError.errorDetail + "//3//" + anError.errorBody)
                        } else {
                            crashlytics.log(it.message!!)
                        }
                        mainView.hideLoading()
                        mainView.showError("Ошибка загрузки информации о филиалах")
                        Timber.tag("my").e(LoggingTree.getMessageForError(it, "LoginPresenter/getAllHospitalBranch "))
                    }
            }
        }

    //createList();
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
                        networkManager2.getCurrentUserInfoInCenter(tmp.idUser!!.toString(), tmp.idBranch!!.toString(),prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
                    }
                        .onSuccess {
                            tmp.surname = MUtils.encodeDecodeWord(
                                it.responses[0].surname!!, it.responses[0].keySurname!!
                            )
                            tmp.name = it.responses[0].name
                            tmp.patronymic = it.responses[0].patronymic
                            tmp.phone = MUtils.encodeDecodeWord(
                                it.responses[0].phone!!,
                                it.responses[0].keyPhone!!
                            )
                            tmp.birthday = it.responses[0].birthday
                            tmp.email = it.responses[0].email
                            if (prefManager.currentUserInfo!!.idUser == tmp.idUser) prefManager.currentUserInfo =
                                tmp
                            var boo = true
                            for (tm in list) {
                                if (tm.name == null) {
                                    boo = false
                                    break
                                }
                            }
                            if (boo) {
                                prefManager.usersLogin = list
                                prefManager.currentUserInfo = list[0]

                                firebaseId
                            }
                        }.onFailure {
                            if (it is ANError) {
                                val anError = it
                                crashlytics.log(anError.errorDetail + "//4//" + anError.errorBody)
                            } else {
                                crashlytics.log(it.message!!)
                            }
                            Timber.tag("my").e(LoggingTree.getMessageForError(it, "LoginPresenter\$getCurrentUserInfo"))
                            mainView.hideLoading()
                            mainView.showError("Ошибка загрузки информации о пользователе")
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
                            .e("LoginPresenter\$getFirebaseId getInstanceId failed " + task.exception)
                        mainView.openProfileActivity()
                        mainView.closeActivity()
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
            mainView.openProfileActivity()
            mainView.closeActivity()
            return
        }

        for (i in list) {
            mainScope.launch {
                kotlin.runCatching {
                    networkManager2.sendFcmId(i.idUser.toString(), i.idBranch.toString(), token, prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
                }
                    .onSuccess {
                        countFcmSend--
                        if(countFcmSend<=0) {
                            mainView.openProfileActivity()
                            mainView.closeActivity()
                        }

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

    fun restorePass(username: String) {
        mainView.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager2.requestNewPass(username)
            }
                .onSuccess {
                    mainView.responseOnPassRequest(it.response)
                    mainView.hideLoading()
                }.onFailure {
                    if (it is ANError) {
                        val anError = it
                        crashlytics.log(anError.errorDetail + "//7//" + anError.errorBody)
                    } else {
                        crashlytics.log(it.message!!)
                    }
                    Timber.tag("my").e(
                        LoggingTree.getMessageForError(
                            it,
                            "LoginPresenter\$restorePass "
                        )
                    )
                    mainView.hideLoading()
                    mainView.showError("Ошибка восстановления пароля")
                }
        }
    }
}