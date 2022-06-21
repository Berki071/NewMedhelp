package com.medhelp.medhelp.ui.settings

import android.content.Context
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.model.settings.SimpleResponseBoolean
import com.medhelp.medhelp.data.model.settings.SimpleResponseString
import com.medhelp.medhelp.data.pref.PreferencesManager

import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.shared.model.SettingsAllBranchHospitalResponse
import com.medhelp.shared.model.UserResponse
import com.medhelp.shared.network.NetworkManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import com.medhelp.medhelp.data.network.NetworkManager as NM

class SettingsPresenter(var context: Context, var viewHelper: SettingsFragment) {

    var prefManager: PreferencesManager
    var networkManager: NM
    val networkManager2 = NetworkManager()

    val mainScope = MainScope()  //+

    init {
        prefManager = PreferencesManager(context)
        networkManager = NM(prefManager)
    }

    fun removePassword() {
        prefManager.currentPassword = ""
        prefManager.usersLogin = null
        prefManager.currentUserInfo = null
    }

    fun unSubscribe() {
        viewHelper!!.hideLoading()
    }

    fun getAllHospitalBranch() {
        viewHelper!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager2.getAllHospitalBranch(prefManager.currentUserInfo!!.idCenter.toString())
            }
                .onSuccess {
                    if (viewHelper == null) {
                        return@onSuccess
                    }
                    viewHelper!!.updateBranchHospital(it.response)
                    viewHelper!!.hideLoading()
                }.onFailure {
                    Timber.tag("my").e(LoggingTree.getMessageForError(it, "SettingsPresenter\$getAllHospitalBranch"))
                    if (viewHelper == null) {
                        return@onFailure
                    }
                    viewHelper!!.hideLoading()
                    viewHelper!!.showErrorScreen()
                }
        }
    }

    fun selectedNewHospitalBranch(newBranch: Int) {
        //Log.wtf("mLog","selectedNewHospitalBranch "+ newBranch);
        if (newBranch != prefManager.currentUserInfo!!.idBranch) {
            //query to db
            viewHelper!!.showLoading()
            val oUser = prefManager.currentUserInfo!!.idUser!!
            val oBranch = prefManager.currentUserInfo!!.idBranch!!
            val cd = CompositeDisposable()
            cd.add(networkManager
                .sendNewFavoriteHospitalBranch(oUser, oBranch, newBranch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: SimpleResponseString ->
                    if (viewHelper == null) {
                        return@subscribe
                    }
                    selectedNewHospitalBranchToInnerBase(newBranch, response.response)
                    cd.dispose()
                }) { throwable: Throwable? ->
                    Timber.tag("my").e(
                        LoggingTree.getMessageForError(
                            throwable,
                            "SettingsPresenter selectedNewHospitalBranch"
                        )
                    )
                    if (viewHelper == null) {
                        return@subscribe
                    }
                    viewHelper!!.hideLoading()
                    viewHelper!!.spinnerRefresh(false)
                    viewHelper!!.showError(R.string.api_default_error)
                    cd.dispose()
                })
        }
    }

    private fun selectedNewHospitalBranchToInnerBase(newBranch: Int, newUserId: String) {
        val cd = CompositeDisposable()
        cd.add(networkManager
            .sendNewFavoriteHospitalBranchToInnerServer(newBranch, newUserId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: SimpleResponseBoolean ->
                if (viewHelper == null) {
                    return@subscribe
                }
                if (response.response) {
                    viewHelper!!.spinnerRefresh(true)
                    saveNewBranchAndUserId(newBranch, newUserId)
                }
                viewHelper!!.hideLoading()
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SettingsPresenter selectedNewHospitalBranchToInnerBase"
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper!!.hideLoading()
                viewHelper!!.spinnerRefresh(false)
                viewHelper!!.showError(R.string.api_default_error)
                cd.dispose()
            })
    }

    private fun saveNewBranchAndUserId(newBranch: Int, newUserId: String) {
        val currentUser = prefManager.currentUserInfo
        val list = prefManager.usersLogin
        for (tmp in list!!) {
            if (tmp.idUser === currentUser!!.idUser) {
                tmp.idUser = Integer.valueOf(newUserId)
                tmp.idBranch = newBranch
                tmp.nameBranch = viewHelper!!.currentNameBranch
                prefManager.usersLogin = list
                prefManager.currentUserInfo = tmp
                break
            }
        }
    }

    fun getNumPositionInListBranch(list: List<SettingsAllBranchHospitalResponse>): Int {
        val br = prefManager.currentUserInfo!!.idBranch!!
        for (i in list.indices) {
            if (list[i].idBranch == br) return i
        }
        return 0
    }

    fun getNeedShowLockScreen(): Boolean {
        return prefManager.screenLock
    }

    fun setNeedShowLockScreen(boo: Boolean) {
        prefManager.screenLock = boo
    }

    fun getUserToken(): String {
        return prefManager.currentUserInfo!!.apiKey!!
    }

    fun getCurrentUser(): UserResponse {
        return prefManager.currentUserInfo!!
    }

    fun setCurrentUser(user: UserResponse) {
        prefManager.currentUserInfo = user
    }

    fun destroy(){
        mainScope.cancel()
    }
}