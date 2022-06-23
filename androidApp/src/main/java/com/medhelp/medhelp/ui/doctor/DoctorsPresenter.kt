package com.medhelp.medhelp.ui.doctor

import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.newmedhelp.model.AllDoctorsResponse
import com.medhelp.shared.model.UserResponse
import com.medhelp.shared.network.NetworkManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList

class DoctorsPresenter(val view: DoctorsFragment) {
    private var allDoc: List<AllDoctorsResponse>? = null
    var prefManager: PreferencesManager
    var networkManager: NetworkManager

    val mainScope = MainScope()

    init {
        prefManager = PreferencesManager(view.requireContext())
        networkManager = NetworkManager()
    }

    fun removePassword() {
        prefManager.currentPassword = ""
        prefManager.usersLogin = null
    }

    fun getSpecialtyByCenter() {
        view!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager.getCategoryApiCall(prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    try {
                        view!!.updateSpecialty(it.spec)
                    } catch (e: Exception) {
                        Timber.tag("my")
                            .e(getMessageForError(e, "DoctorsPresenter\$getSpecialtyByCenter$1 "))
                    }
                }.onFailure {
                    view!!.hideLoading()
                    Timber.tag("my").e(getMessageForError(it, "DoctorsPresenter\$getSpecialtyByCenter$2 "))
                    view!!.showErrorScreen()
                }
        }
    }

    fun getDoctorList(idSpec: Int) {
        if (idSpec == -1 && allDoc != null) {
            view!!.updateView(allDoc!!)
            view!!.hideLoading()
            return
        }
        if (allDoc != null) {
            view!!.updateView(sortAllDoc(idSpec))
            view!!.hideLoading()
            return
        }
        view!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager.getAllDoctors(
                    prefManager.currentUserInfo!!.apiKey!!,
                    prefManager.centerInfo!!.db_name!!,
                    prefManager.currentUserInfo!!.idUser.toString(),
                    prefManager.currentUserInfo!!.idBranch.toString()
                )
            }
                .onSuccess {
                    if (it.mResponses.size <= 0) {
                        view!!.updateView(null)
                    } else {
                        allDoc = it.mResponses
                        if (view != null) view!!.updateView(it.mResponses)
                    }
                    view!!.hideLoading()
                }.onFailure {
                    Timber.tag("my").e(getMessageForError(it, "DoctorsPresenter\$getDoctorList "))
                    view!!.hideLoading()
                    view!!.showErrorScreen()
                }
        }
    }

    private fun sortAllDoc(idSpec: Int): List<AllDoctorsResponse> {
        val sortList: MutableList<AllDoctorsResponse> = ArrayList()
        for (i in allDoc!!.indices) {
            val doc = allDoc!![i]
            if (doc.getIdSpecialtiesIntList() == null) continue
            if (doc.getIdSpecialtiesIntList()!!.size == 1 && doc.getIdSpecialtiesIntList()!![0] == idSpec) {
                sortList.add(doc)
                continue
            }
            for (j in doc.getIdSpecialtiesIntList()!!.indices) {
                if (doc.getIdSpecialtiesIntList()!![j] == idSpec) {
                    sortList.add(doc)
                }
            }
        }
        return sortList
    }

    fun unSubscribe() {
        view!!.hideLoading()
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

}