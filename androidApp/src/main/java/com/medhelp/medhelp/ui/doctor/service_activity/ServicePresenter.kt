package com.medhelp.medhelp.ui.doctor.service_activity

import com.medhelp.medhelp.data.model._heritable.ServiceResponseAndroid
import com.medhelp.medhelp.data.pref.PreferencesManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import com.medhelp.newmedhelp.model.CategoryResponse
import com.medhelp.medhelp.data.model.osn.CheckSpamRecordList
import com.medhelp.medhelp.data.model.settings.SimpleResponseBoolean
import com.medhelp.medhelp.data.network.NetworkManager
import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.medhelp.shared.network.NetworkManager as NewNetworkManager
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ServicePresenter(val view: ServiceActivity) {

    var prefManager: PreferencesManager
    var networkManager: NetworkManager
    var newNetworkManager = NewNetworkManager()

    val mainScope = MainScope()

    init {
        prefManager = PreferencesManager(view)
        networkManager = NetworkManager(prefManager)
    }

    fun getData(idDoctor: Int, idBranch: Int, idUser: Int) {
        view!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                newNetworkManager.getCategoryApiCall(
                    idDoctor,
                    prefManager.currentUserInfo!!.apiKey!!,
                    prefManager.centerInfo!!.db_name!!,
                    prefManager.currentUserInfo!!.idUser.toString(),
                    prefManager.currentUserInfo!!.idBranch.toString()
                )
            }
                .onSuccess {
                    if (it != null) {
                        getPrice(it.spec, idDoctor, idBranch, idUser)
                    } else {
                        view!!.hideLoading()
                    }
                }.onFailure {
                    Timber.tag("my").e(getMessageForError(it, "ServicePresenter\$getFilesForRecy "))
                    if (view == null) {
                        return@onFailure
                    }
                    view!!.hideLoading()
                    view!!.showErrorScreen()
                }
        }
    }

    private fun getPrice(categoryResponse: List<CategoryResponse>, idDoctor: Int, idBranch: Int, idUser: Int) {

        mainScope.launch {
            kotlin.runCatching {
                newNetworkManager.getPriceApiCall(
                    idDoctor, idBranch, idUser,
                    prefManager.currentUserInfo!!.apiKey!!,
                    prefManager.centerInfo!!.db_name!!,
                    prefManager.currentUserInfo!!.idUser.toString(),
                    prefManager.currentUserInfo!!.idBranch.toString()
                )
            }
                .onSuccess {
                    var listNew = mutableListOf<ServiceResponseAndroid>()
                    for(i in it.services!!){
                        listNew.add(ServiceResponseAndroid(i))
                    }

                    view!!.updateView(categoryResponse, listNew)
                    view!!.hideLoading()
                }.onFailure {
                    Timber.tag("my").e(getMessageForError(it, "ServicePresenter\$getPrice "))
                    if (view == null) {
                        return@onFailure
                    }
                    view!!.hideLoading()
                    view!!.showErrorScreen()
                }
        }
    }

    fun changeFabFavorites(item: ServiceResponseAndroid) {
        if (item.favorites == "1") {
            val cd = CompositeDisposable()
            cd.add(networkManager
                .insertFavoritesService(item.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response: SimpleResponseBoolean? ->
                        Timber.tag("my")
                            .v("Добавление в закладки: id услуги " + item.id + ", название услуги " + item.title)
                        cd.dispose()
                    }
                ) { throwable: Throwable? ->
                    Timber.tag("my").e(getMessageForError(throwable, "ServicePresenter\$changeFabFavorites$1 "))
                    item.favorites = "0"
                    view!!.refreshRecy()
                    view!!.showError("Не удалось сохранить в избранное")
                    // Log.wtf("mLog", throwable.getMessage());
                    cd.dispose()
                }
            )
        } else {
            val cd = CompositeDisposable()
            cd.add(networkManager
                .deleteFavoritesService(item.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response: SimpleResponseBoolean? -> cd.dispose() }
                ) { throwable: Throwable? ->
                    Timber.tag("my").e(
                        getMessageForError(
                            throwable,
                            "ServicePresenter\$changeFabFavorites$2 "
                        )
                    )
                    item.favorites = "1"
                    view!!.refreshRecy()
                    view!!.showError("Не удалось удалить из избранного")
                    //Log.wtf("mLog", throwable.getMessage());
                    cd.dispose()
                }
            )
        }
    }

    fun unSubscribe() {
        view!!.hideLoading()
    }

    fun testToSpam(view2: ServiceAdapter.ViewHolder, service: Int, limitService: Int) {
        view!!.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .checkSpamRecord(service)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: CheckSpamRecordList ->
                    val limitCenter = prefManager.centerInfo!!.max_records

                    /*  int co1=response.getmResponses().get(0).getCount1();
                            int co2=response.getmResponses().get(0).getCount2();*/
                    if (limitService < response._mResponses[0].count1 || limitCenter < response._mResponses[0].count2) {
                    view!!.showError("Превышен лимит записей, для получения более подробной информации обратитесь в медицинский центр")
                } else {
                    view2.jumpToNextPage()
                }
                    view!!.hideLoading()
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                Timber.tag("my").e(getMessageForError(throwable, "ServicePresenter\$testToSpam "))
                view!!.hideLoading()
                view!!.showError("Не удалось проверить на спам")
                cd.dispose()
            }
        )
    }

}