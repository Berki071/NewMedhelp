package com.medhelp.medhelp.ui._main_page

import android.content.Context
import com.medhelp.medhelp.data.model._heritable.BonusesItemAndroid
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.utils.main.MainUtils
import com.medhelp.shared.model.UserResponse
import com.medhelp.shared.network.NetworkManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivityPresenter(var context: Context) : MainActivityHelper.Presenter {
    var networkManager = NetworkManager()
    @JvmField
    var prefManager: PreferencesManager
    var view: MainActivityHelper.View
    var bonusesItemList: List<BonusesItemAndroid>? = null

    val mainScope = MainScope()

    init {
        prefManager = PreferencesManager(context)
        view = context as MainActivity
    }

    override fun getCurrentUser(): UserResponse {
        return prefManager.currentUserInfo!!
    }

    override fun setCurrentUser(user: UserResponse) {
        prefManager.currentUserInfo = user
    }

    fun getLogin(): String? {
        return prefManager.currentLogin
    }

    override fun removePassword() {
        prefManager.currentPassword = ""
        prefManager.usersLogin = null
        prefManager.currentUserInfo = null
    }

    fun getAllBonuses() {
        mainScope.launch {
            kotlin.runCatching {
                networkManager.getAllBonuses(prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.centerInfo!!.idCenter!!.toString())
            }
                .onSuccess {
                    if (it.response!!.size == 1 && it.response!![0].date == null) {
                        bonusesItemList = null
                        view.initBonuses(null)
                    } else {
                        var newList : MutableList<BonusesItemAndroid> = mutableListOf()
                        for(i in it.response!!){
                            newList.add(BonusesItemAndroid(i))
                        }

                        bonusesItemList = newList
                        Collections.sort(newList)
                        view.initBonuses(MainUtils.getSumBonuses(newList))
                    }
                }.onFailure {
                    bonusesItemList = null
                    view.initBonuses(null)

                }
        }
    }

}