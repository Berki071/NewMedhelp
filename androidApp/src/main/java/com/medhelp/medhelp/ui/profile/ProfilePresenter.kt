package com.medhelp.medhelp.ui.profile

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.medhelp.medhelp.data.model.DataPaymentForRealm
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.newmedhelp.model.VisitResponseAndroid
import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.medhelp.medhelp.utils.TimesUtils
import com.medhelp.newmedhelp.model.VisitResponse
import com.medhelp.shared.model.UserResponse
import com.medhelp.shared.network.NetworkManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList

class ProfilePresenter(val context: Context, val viewHelper: ProfileFragment){
    var prefManager: PreferencesManager
    var networkManager: NetworkManager

    val mainScope = MainScope()

    init {
        prefManager = PreferencesManager(context)
        networkManager = NetworkManager()
    }

    fun getVisits1() {
        viewHelper!!.showLoading()
        mainScope.launch {
            kotlin.runCatching {
                networkManager.getCurrentDateApiCall(prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    getVisits2(it.response!!.time!!, it.response!!.today!!)
                }.onFailure {
                    Timber.tag("my").e(getMessageForError(it, "ProfilePresenter\$getVisits1 "))
                    if (viewHelper == null) return@onFailure
                    viewHelper!!.hideLoading()
                    viewHelper!!.swipeDismiss()
                    viewHelper!!.showErrorScreen()
                }
        }
    }

    fun getVisits2(time : String, today : String){

        mainScope.launch {
            kotlin.runCatching {
                networkManager.getAllReceptionApiCall(prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    if (viewHelper == null) return@onSuccess

                    if (!it.error) {
                        var listAndr = mutableListOf<VisitResponseAndroid>()
                        for(i in it.response){
                            listAndr.add(VisitResponseAndroid(i))
                        }

                        viewHelper!!.updateData(listAndr, today, time)
                    }else
                        viewHelper!!.showErrorScreen()
                    viewHelper!!.hideLoading()
                    viewHelper!!.swipeDismiss()
                }.onFailure {
                    Timber.tag("my").e(getMessageForError(it, "ProfilePresenter\$getVisits2 "))
                    if (viewHelper == null) return@onFailure
                    viewHelper!!.hideLoading()
                    viewHelper!!.swipeDismiss()
                    viewHelper!!.showErrorScreen()
                }
        }
    }

    fun updateHeaderInfo() {
        prefManager.centerInfo?.let {
            viewHelper!!.updateHeader(it)
        }
    }

    fun unSubscribe() {
        viewHelper!!.hideLoading()
    }

    fun cancellationOfVisit(idUser: Int, id_record: Int, cause: String, idBranch: Int) {
        viewHelper!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager.sendCancellationOfVisit(
                    idUser.toString(),
                    id_record.toString(),
                    cause,
                    TimesUtils.getCurrentDate(TimesUtils.DATE_FORMAT_ddMMyy),
                    idBranch.toString(),
                    prefManager.currentUserInfo!!.apiKey!!,
                    prefManager.centerInfo!!.db_name!!,
                    prefManager.currentUserInfo!!.idUser.toString(),
                    prefManager.currentUserInfo!!.idBranch.toString()
                )
            }
                .onSuccess {
                    Timber.tag("my").v("Отмена приема id записи $id_record причина $cause")
                    getVisits1()
                }.onFailure {
                    viewHelper!!.hideLoading()
                    Timber.tag("my")
                        .e(getMessageForError(it, "ProfilePresenter\$cancellationOfVisit "))
                    viewHelper!!.showError("Произошла ошибка при выполнении операции, попробуйте повторить")
                }
        }
    }

    fun confirmationOfVisit(idUser: Int, id_record: Int, idBranch: Int) {
        viewHelper!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager.sendConfirmationOfVisit(
                    idUser.toString(),
                    id_record.toString(),
                    idBranch.toString(),
                    prefManager.currentUserInfo!!.apiKey!!,
                    prefManager.centerInfo!!.db_name!!,
                    prefManager.currentUserInfo!!.idUser.toString(),
                    prefManager.currentUserInfo!!.idBranch.toString()
                )
            }
                .onSuccess {
                    Timber.tag("my").v("Подтвердение приема id записи %s", id_record)
                    if (it.response) {
                        getVisits1()
                    }
                }.onFailure {
                    Timber.tag("my")
                        .e(getMessageForError(it, "ProfilePresenter\$confirmationOfVisit "))
                    viewHelper!!.showError("Произошла ошибка при выполнении операции, попробуйте повторить")
                    viewHelper!!.hideLoading()
                }
        }
    }

    fun getCurrentHospitalBranch(): String {
        //String ss=getDataHelper().getCurrentNameBranch();
        return prefManager.currentUserInfo!!.nameBranch!!
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

    fun getCountItems(element: String): Int {
        return element.length - element.replace("&", "").length
    }

    fun sendToServer(data: DataPaymentForRealm) {
        Timber.tag("my").v("услуги оплачены, Sum: " + data.price + "; IdRecord: " + data.idZapisi + "; IdPayment: " + data.idPayment + "; IdYsl: " + data.idYsl)

        mainScope.launch {
            kotlin.runCatching {
                networkManager.sendToServerPaymentData(data.idUser, data.idBranch, data.idZapisi, data.idYsl, data.price, getCountItems(data.idUser).toString(), data.idPayment,
                    prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    if (it.response=="true") {
                    } else {
                        val gson = Gson()
                        Timber.tag("my").e(getMessageForError(null, "ProfilePresenter/sendPaymentToServer, совпал pay_id " + gson.toJson(data)))
                    }
                    prefManager.deletePaymentData(data)
                }.onFailure {
                    val gson = Gson()
                    Timber.tag("my").e(getMessageForError(it, "ProfilePresenter/sendPaymentToServer : " + gson.toJson(data)))
                }
        }
    }

    fun sendConfirmComing(viz: VisitResponseAndroid) {
        viewHelper!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager.sendIAmHere(viz.idUser.toString(), viz.idRecord.toString(), viz.idBranch.toString(),
                    prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    viewHelper!!.hideLoading()
                    viewHelper!!.responseConfirmComing(viz)
                }.onFailure {
                    viewHelper!!.hideLoading()
                    Timber.tag("my").e(getMessageForError(it, "ProfilePresenter/sendConfirmComing "))
                }
        }
    }
}