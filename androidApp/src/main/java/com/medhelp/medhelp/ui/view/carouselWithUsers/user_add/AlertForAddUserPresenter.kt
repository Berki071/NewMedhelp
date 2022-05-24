package com.medhelp.medhelp.ui.view.carouselWithUsers.user_add

import android.content.Context
import android.os.Handler
import com.medhelp.medhelp.data.network.ProtectionData
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.shared.model.UserResponse
import com.medhelp.shared.network.NetworkManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import com.medhelp.medhelp.data.network.NetworkManager as NM

class AlertForAddUserPresenter(var context: Context, var view: AlertForAddUser)  {
    var prefManager: PreferencesManager = PreferencesManager(context)
    var networkManager = NM(prefManager)
    val networkManager2 = NetworkManager()

    val mainScope = MainScope() //+

    fun sendPhoneForSMS(phone: String) {
        view.showLoading()
        val handler = Handler()
        val delay = 1000 //milliseconds
        handler.postDelayed({
            view.hideLoading()
            view.responsePhoneSent(true)
        }, delay.toLong())

    }

    fun sendLoginAndPassword(login: String, password: String) {
        view.showLoading()
        val handler = Handler()
        val delay = 1000 //milliseconds
        handler.postDelayed({ refreshUsers() }, delay.toLong())
    }

    private fun refreshUsers() {
        val username = prefManager.currentLogin
        val password = prefManager.currentPassword
        val cd = CompositeDisposable()


        mainScope.launch {
            kotlin.runCatching {
                networkManager2.doLoginApiCall(ProtectionData().getSignature(view.context)!!, username!!, password!!)
            }
                .onSuccess {
                    if (it.response[0].login != null) {
                        val oldList = prefManager.usersLogin
                        prefManager.usersLogin = it.response
                        prefManager.currentUserInfo = getNewUser(oldList, it.response)
                        currentUserInfo
                    } else {
                        view.errorRefreshUsers()
                        Timber.tag("my").e(
                            LoggingTree.getMessageForError(
                                null,
                                "AlertForAddUserPresenter\$refreshUsers получен пустой список"
                            )
                        )
                    }
                }.onFailure {
                    view.hideLoading()
                    view.errorRefreshUsers()
                    Timber.tag("my").e(
                        LoggingTree.getMessageForError(
                            it,
                            "AlertForAddUserPresenter\$refreshUsers"
                        )
                    )
                }
        }
    }

    private val currentUserInfo: Unit
        private get() {
            val list = prefManager.usersLogin
            for (tmp in list!!) {

                mainScope.launch {
                    kotlin.runCatching {
                        networkManager2.getCurrentUserInfoInCenter(tmp.idUser!!, tmp.idBranch!!,prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
                    }
                        .onSuccess {
                            tmp.surname = it.responses[0].surname
                            tmp.name = it.responses[0].name
                            tmp.patronymic = it.responses[0].patronymic
                            tmp.phone = it.responses[0].phone
                            tmp.birthday = it.responses[0].birthday
                            tmp.email = it.responses[0].email
                            if (prefManager.currentUserInfo!!.idUser === tmp.idUser) prefManager.currentUserInfo =
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
                                view.responsePasswordSent(true)
                                view.hideLoading()
                            }
                        }.onFailure {
                            view.hideLoading()
                            Timber.tag("my").e(LoggingTree.getMessageForError(it, "AlertForAddUserPresenter\$getCurrentUserInfo "))
                        }
                }
            }
        }

    private fun getNewUser(
        oldList: List<UserResponse>?,
        newList: List<UserResponse>
    ): UserResponse {
        for (i in newList.indices.reversed()) {
            var isFind = false
            for (tmp in oldList!!) {
                if (tmp.idUser === newList[i].idUser && tmp.idBranch === newList[i].idBranch) {
                    isFind = true
                    break
                }
            }
            if (!isFind) {
                return newList[i]
            }
        }
        return newList[newList.size - 1]
    }
}