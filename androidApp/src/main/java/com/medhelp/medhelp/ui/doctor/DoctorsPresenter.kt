package com.medhelp.medhelp.ui.doctor

import android.content.Context
import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.medhelp.medhelp.data.model.AllDoctorsResponse
import com.medhelp.medhelp.data.pref.PreferencesManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import com.medhelp.medhelp.data.model.AllDoctorsList
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.medhelp.data.model.CategoryResponse
import com.medhelp.medhelp.data.model.SpecialtyList
import com.medhelp.medhelp.data.network.NetworkManager
import com.medhelp.shared.model.UserResponse
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.lang.Exception
import java.util.ArrayList

class DoctorsPresenter(val view: DoctorsFragment) {
    private var allDoc: List<AllDoctorsResponse>? = null
    var prefManager: PreferencesManager
    var networkManager: NetworkManager

    init {
        prefManager = PreferencesManager(view.requireContext())
        networkManager = NetworkManager(prefManager)
    }

    fun removePassword() {
        prefManager.currentPassword = ""
        prefManager.usersLogin = null
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
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getAllDoctors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: AllDoctorsList ->
                    if (response.responses.size <= 0) {
                        view!!.updateView(null)
                    } else {
                        allDoc = response.responses
                        if (view != null) view!!.updateView(response.responses)
                    }
                    view!!.hideLoading()
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                Timber.tag("my").e(getMessageForError(throwable, "DoctorsPresenter\$getDoctorList "))
                view!!.hideLoading()
                view!!.showErrorScreen()
                cd.dispose()
            }
        )
    }

    private fun sortAllDoc(idSpec: Int): List<AllDoctorsResponse> {
        val sortList: MutableList<AllDoctorsResponse> = ArrayList()
        for (i in allDoc!!.indices) {
            val doc = allDoc!![i]
            if (doc.id_specialties_int_list == null) continue
            if (doc.id_specialties_int_list.size == 1 && doc.id_specialties_int_list[0] == idSpec) {
                sortList.add(doc)
                continue
            }
            for (j in doc.id_specialties_int_list.indices) {
                if (doc.id_specialties_int_list[j] == idSpec) {
                    sortList.add(doc)
                }
            }
        }
        return sortList
    }

    fun getSpecialtyByCenter() {
        view!!.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getCategoryApiCall()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { obj: SpecialtyList -> obj.spec }
            .subscribe({ response: List<CategoryResponse> ->
                try {
                    view!!.updateSpecialty(response)
                } catch (e: Exception) {
                    Timber.tag("my")
                        .e(getMessageForError(e, "DoctorsPresenter\$getSpecialtyByCenter$1 "))
                }
                cd.dispose()
            }) { throwable: Throwable? ->
                view!!.hideLoading()
                Timber.tag("my").e(
                    getMessageForError(
                        throwable,
                        "DoctorsPresenter\$getSpecialtyByCenter$2 "
                    )
                )
                view!!.showErrorScreen()
                cd.dispose()
            })
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