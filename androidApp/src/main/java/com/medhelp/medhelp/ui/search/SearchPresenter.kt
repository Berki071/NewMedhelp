package com.medhelp.medhelp.ui.search

import android.content.Context
import com.medhelp.medhelp.data.model._heritable.ServiceResponseAndroid
import com.medhelp.medhelp.utils.timber_log.LoggingTree.Companion.getMessageForError
import com.medhelp.medhelp.data.pref.PreferencesManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.newmedhelp.model.CategoryResponse
import com.medhelp.medhelp.data.model.osn.CheckSpamRecordList
import com.medhelp.medhelp.data.model.settings.SimpleResponseBoolean
import com.medhelp.medhelp.data.network.NetworkManager
import com.medhelp.medhelp.ui.search.recy_spinner.SearchAdapter
import com.medhelp.newmedhelp.model.ServiceResponse
import com.medhelp.shared.model.UserResponse
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import com.medhelp.shared.network.NetworkManager as NewNetworkManager

class SearchPresenter(val view: SearchFragment) {

    var prefManager: PreferencesManager
    var networkManager: NetworkManager
    var newNetworkManager = NewNetworkManager()
    val mainScope = MainScope()

    init {
        prefManager = PreferencesManager(view.requireContext())
        networkManager = NetworkManager(prefManager)
    }

    fun getData() {
        view!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                newNetworkManager.getCategoryApiCall(
                    prefManager.currentUserInfo!!.apiKey!!,
                    prefManager.centerInfo!!.db_name!!,
                    prefManager.currentUserInfo!!.idUser.toString(),
                    prefManager.currentUserInfo!!.idBranch.toString()
                )
            }
                .onSuccess {
                    if (it != null) {
                        getPrice(it.spec)
                    } else {
                        view!!.hideLoading()
                    }
                }.onFailure {
                    Timber.tag("my")
                        .e(getMessageForError(it, "SearchPresenter\$getFilesForRecy "))
                    if (view == null) {
                        return@onFailure
                    }
                    view!!.hideLoading()
                    view!!.showErrorScreen()
                }
        }
    }

    private fun getPrice(categoryResponse: List<CategoryResponse>) {
        view!!.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                newNetworkManager.getPriceApiCall(
                    prefManager.currentUserInfo!!.apiKey!!,
                    prefManager.centerInfo!!.db_name!!,
                    prefManager.currentUserInfo!!.idUser.toString(),
                    prefManager.currentUserInfo!!.idBranch.toString()
                )
            }
                .onSuccess {
                    if (it.services != null && it.services!![0].title != null) {
                        var listNew = mutableListOf<ServiceResponseAndroid>()

                        for(i in it.services!!){
                            listNew.add(ServiceResponseAndroid(i))
                        }

                        view!!.updateView(categoryResponse, listNew)
                    }
                    view!!.hideLoading()
                }.onFailure {
                    Timber.tag("my").e(getMessageForError(it, "SearchPresenter\$getPrice"))
                    if (view == null) {
                        return@onFailure
                    }
                    view!!.hideLoading()
                    view!!.showErrorScreen()
                }
        }
    }

    fun unSubscribe() {
        view!!.hideLoading()
    }

    fun getUserToken(): String {
        return prefManager.currentUserInfo!!.apiKey!!
    }

    fun changeFabFavorites(item: ServiceResponse) {
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
                    Timber.tag("my").e(
                        getMessageForError(
                            throwable,
                            "SearchPresenter\$changeFabFavorites$1 "
                        )
                    )
                    item.favorites = "0"
                    view!!.refreshRecy()
                    view!!.showError("Не удалось сохранить в избранное")
                    //Log.wtf("mLog", throwable.getMessage());
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
                            "SearchPresenter\$changeFabFavorites$2 "
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

    fun testToSpam(view2: SearchAdapter.ViewHolder, service: Int, limitService: Int) {
        view!!.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .checkSpamRecord(service)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: CheckSpamRecordList ->
                    val limitCenter = prefManager.centerInfo!!.max_records
                    val co1 = response._mResponses[0].count1
                    val co2 = response._mResponses[0].count2
                    if (limitService < response._mResponses[0].count1 || limitCenter < response._mResponses[0].count2) {
                        view!!.showError("Превышен лимит записей, для получения более подробной информации обратитесь в медицинский центр")
                    } else {
                        view2.jumpToNextPage()
                    }
                    view!!.hideLoading()
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                Timber.tag("my").e(getMessageForError(throwable, "SearchPresenter\$testToSpam "))
                view!!.hideLoading()
                view!!.showError("Не удалось проверить на спам")
                cd.dispose()
            }
        )
    }

    fun getCurrentUser(): UserResponse {
        return prefManager.currentUserInfo!!
    }

    fun setCurrentUser(user: UserResponse) {
        prefManager.currentUserInfo = user
    }
}