package com.medhelp.medhelp.ui.analysis_price_list

import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.newmedhelp.model.AnalisePriceResponse
import io.reactivex.disposables.CompositeDisposable

import com.medhelp.shared.model.UserResponse
import com.medhelp.shared.network.NetworkManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class AnalysisPriceListPresenter(val mainView: AnalysisPriceListFragment) {
    var networkManager = NetworkManager()
    var prefManager = PreferencesManager (mainView.requireContext())
    val mainScope = MainScope()

    fun getAnalisePrice() {
        mainView.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager.getAnalisePrice(prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    val group = separateGroup(it.response)
                    mainView.updateView(group, it.response)
                    mainView.hideLoading()
                }.onFailure {
                    mainView.hideLoading()
                    Timber.tag("my").e(getMessageForError(it, "AnalysisPriceListPresenter\$getAnalisePrice "))
                    mainView.showErrorScreen()
                }
        }
    }

    fun removePassword() {
        prefManager.currentPassword = ""
        prefManager.usersLogin = null
        prefManager.currentUserInfo = null
    }

    fun getUserToken(): String {
        return prefManager.currentUserInfo!!.apiKey!!
    }

    private fun separateGroup(list: List<AnalisePriceResponse>): List<String> {
        val groupList: MutableList<String> = ArrayList()
        groupList.add(list[0].group!!)
        for (tmp in list) {
            var overlap = false
            for (str in groupList) {
                if (str == tmp.group) {
                    overlap = true
                    break
                }
            }
            if (!overlap) {
                groupList.add(tmp.group!!)
            }
        }
        Collections.sort(groupList)
        return groupList
    }

    fun getCurrentUser(): UserResponse {
        return prefManager.currentUserInfo!!
    }

    fun setCurrentUser(user: UserResponse) {
        prefManager.currentUserInfo = user
    }
}