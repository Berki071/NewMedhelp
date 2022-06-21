package com.medhelp.medhelp.ui.finances_and_services

import com.medhelp.medhelp.data.pref.PreferencesManager
import io.reactivex.disposables.CompositeDisposable
import com.medhelp.newmedhelp.model.DateList
import io.reactivex.android.schedulers.AndroidSchedulers
import com.medhelp.medhelp.data.model.DataPaymentForRealm
import com.medhelp.medhelp.data.model.yandex_cashbox.PaymentInformationModel
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.medhelp.medhelp.data.model.chat.SimpleResBoolean
import com.medhelp.medhelp.data.network.NetworkManager

import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.medhelp.newmedhelp.model.VisitResponse
import com.medhelp.newmedhelp.model.VisitResponseAndroid
import com.medhelp.shared.model.UserResponse
import com.medhelp.shared.network.NetworkManager as NewNetworkManager
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList

class FinancesAndServicesPresenter(val mainView: FinancesAndServicesFragment) {
    var prefManager: PreferencesManager
    var networkManager: NetworkManager
    val networkManager2 =  NewNetworkManager()
    val mainScope = MainScope()

    init {
        prefManager = PreferencesManager(mainView.requireContext())
        networkManager = NetworkManager(prefManager)
        realmPaymentData
    }

    fun removePassword() {
        prefManager.currentPassword = ""
        prefManager.usersLogin = null
        prefManager.currentUserInfo = null
    }

    fun getVisits1() {

        mainView.showLoading()
        mainScope.launch {
            kotlin.runCatching {
                networkManager2.getCurrentDateApiCall(prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    getVisits2(it.response!!.time!!, it.response!!.today!!)
                }.onFailure {
                    Timber.tag("my")
                        .e(getMessageForError(it, "ProfilePresenter\$getVisits1"))
                    mainView.hideLoading()
                    mainView.showErrorScreen()
                }
        }
    }


    fun getVisits2(time : String, today : String){

        mainScope.launch {
            kotlin.runCatching {
                networkManager2.getAllReceptionApiCall(prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    if (!it.error) {
                        var listAndr = mutableListOf<VisitResponseAndroid>()
                        for(i in it.response){
                            listAndr.add(VisitResponseAndroid(i))
                        }

                        mainView.updateData(listAndr, today, time)
                    }else
                        mainView.showErrorScreen()
                    mainView.hideLoading()
                }.onFailure {
                    Timber.tag("my").e(getMessageForError(it, "ProfilePresenter\$getVisits2"))
                    mainView.hideLoading()
                    mainView.showErrorScreen()
                }
        }
    }

    private val realmPaymentData: Unit
        private get() {
            val list: List<DataPaymentForRealm>? = prefManager.getAllPaymentData()
            if (list!!.size != 0) {
                val listToYandex: MutableList<DataPaymentForRealm> = ArrayList()
                val listToServer: MutableList<DataPaymentForRealm> = ArrayList()
                for (tmp in list) {
                    if (tmp.yandexInformation) {
                        listToYandex.add(tmp)
                    } else {
                        listToServer.add(tmp)
                    }
                }
                if (listToYandex.size > 0) {
                    for (tmp in listToYandex) {
                        testPaidToYandex(tmp)
                    }
                }
                if (listToServer.size > 0) {
                    for (tmp in listToServer) {
                        sendToServer(tmp)
                    }
                }
            }
        }

    private fun testPaidToYandex(data: DataPaymentForRealm) {
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getPaymentInformation(data.idPayment, data.yKeyObt.idShop, data.yKeyObt.keyShop)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: PaymentInformationModel ->
                    when (response.status) {
                        "waiting_for_capture" -> sendToServer(data)
                        "succeeded" -> sendToServer(data)
                        else -> Timber.tag("my").e(
                            getMessageForError(
                                null,
                                "FinancesAndServicesPresenter/makingPayment default  response.getStatus()" + response.status + "; response.getId()(idPayment) "
                                        + response.id + "; response.getPaid() " + response.paid
                            )
                        )
                    }
                    prefManager.deletePaymentData(data)
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                if (throwable is ANError) {
                    val msg = throwable.errorBody
                    if (msg != null && msg.contains("not found")) {
                        prefManager.deletePaymentData(data)
                        cd.dispose()
                        return@subscribe
                    }
                }
                val gson = Gson()
                Timber.tag("my").e(
                    getMessageForError(
                        throwable,
                        "FinancesAndServicesPresenter/getPaymentInformation " + gson.toJson(data)
                    )
                )
                cd.dispose()
            }
        )
    }

    private fun getCountItems(element: String): Int {
        return element.length - element.replace("&", "").length
    }

    private fun sendToServer(data: DataPaymentForRealm) {
        Timber.tag("my")
            .v("услуги оплачены, Sum: " + data.price + "; IdZapisi: " + data.idZapisi + "; IdPayment: " + data.idPayment + "; IdYsl: " + data.idYsl)
        val cd = CompositeDisposable()
        cd.add(networkManager
            .sendToServerPaymentData(
                data.idUser,
                data.idBranch,
                data.idZapisi,
                data.idYsl,
                data.price,
                getCountItems(data.idUser),
                data.idPayment
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: SimpleResBoolean ->
                    if (response.response) {
                    } else {
                        val gson = Gson()
                        Timber.tag("my").e(
                            getMessageForError(
                                null,
                                "FinancesAndServicesPresenter/sendPaymentToServer, совпал pay_id " + gson.toJson(
                                    data
                                )
                            )
                        )
                    }
                    prefManager.deletePaymentData(data)
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                val gson = Gson()
                Timber.tag("my").e(
                    getMessageForError(
                        throwable,
                        "FinancesAndServicesPresenter/sendPaymentToServer : " + gson.toJson(data)
                    )
                )
                cd.dispose()
            }
        )
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