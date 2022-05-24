package com.medhelp.medhelp.ui.schedule

import android.content.Context
import com.medhelp.medhelp.R
import com.medhelp.medhelp.data.model.*
import com.medhelp.medhelp.data.model.settings.SimpleResponseString
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.utils.timber_log.LoggingTree
import com.medhelp.shared.network.NetworkManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import com.medhelp.medhelp.data.network.NetworkManager as NM

class SchedulePresenter(val context: Context, val viewHelper: ScheduleFragment) {
    @kotlin.jvm.JvmField
    val prefManager: PreferencesManager = PreferencesManager(context)
    val networkManager = NM(prefManager)
    val networkManager2 = NetworkManager()

    var idUserFavouriteBranch: String? = null

    private val mainScope = MainScope() //+

    private val iddUser: Unit
        private get() {
            val id: Int = prefManager.currentUserInfo!!.idUser!!
            idUserFavouriteBranch = id.toString()
        }

    fun getUserToken(): String? {
        return prefManager.currentUserInfo!!.apiKey
    }

    fun removePassword() {
        prefManager.currentPassword = ""
        prefManager.usersLogin = null
        prefManager.currentUserInfo = null
    }

    fun getDateFromService(idService: Int, adm: Int, idBranch: Int) {
        if (!viewHelper.isLoading()) viewHelper.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getCurrentDateApiCall()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: DateList ->
                if (viewHelper == null) {
                    return@subscribe
                }
                val date = response.response
                getScheduleByService(idService, date, adm, idBranch)
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$getDateFromService "
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper.hideLoading()
                viewHelper.showErrorScreen()
                cd.dispose()
            })
    }

    fun getScheduleByService(idService: Int, date: DateResponse, adm: Int, idBranch: Int) {
        if (!viewHelper.isLoading()) viewHelper.showLoading()
        var monday = date.lastMonday
        if (monday == "1") {
            monday = date.today
        }
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getScheduleByServiceApiCall(idService, monday, adm, idBranch)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .map { obj: ScheduleList -> obj.response }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: List<ScheduleResponse>? ->
                viewHelper.hideLoading()
                viewHelper.setupCalendar(date, response)
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$getScheduleByService "
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper.hideLoading()
                viewHelper.showErrorScreen()
                cd.dispose()
            })
    }

    fun getDateFromDoctor(idDoctor: Int, idService: Int, adm: Int, idBranch: Int) {
        if (!viewHelper.isLoading()) viewHelper.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getCurrentDateApiCall()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: DateList ->
                if (viewHelper == null) {
                    return@subscribe
                }
                val date = response.response
                getScheduleByDoctor(idDoctor, date, adm, idBranch)
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$getDateFromDoctor "
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper.hideLoading()
                viewHelper.showErrorScreen()
                cd.dispose()
            })
    }

    fun getScheduleByDoctor(idDoctor: Int, date: DateResponse, adm: Int, idBranch: Int) {
        if (!viewHelper.isLoading()) viewHelper.showLoading()
        var monday = date.lastMonday
        if (monday == "1") {
            monday = date.today
        }
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getScheduleByDoctorApiCall(idDoctor, monday, adm, idBranch)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .map { obj: ScheduleList -> obj.response }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: List<ScheduleResponse>? ->
                viewHelper.hideLoading()
                viewHelper.setupCalendar(date, response)
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$getScheduleByDoctor1 "
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper.hideLoading()
                viewHelper.showErrorScreen()
                cd.dispose()
            })
    }

    fun getScheduleByDoctor(idDoctor: Int, date: String?, adm: Int, idBranch: Int) {
        if (!viewHelper.isLoading()) viewHelper.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getScheduleByDoctorApiCall(idDoctor, date!!, adm, idBranch)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .map { obj: ScheduleList -> obj.response }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: List<ScheduleResponse>? ->
                viewHelper.hideLoading()
                viewHelper.updateCalendar(date, response)
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$getScheduleByDoctor2 "
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper.hideLoading()
                viewHelper.showErrorScreen()
                cd.dispose()
            })
    }

    fun getScheduleByService(idService: Int, date: String?, adm: Int, idBranch: Int) {
        if (!viewHelper.isLoading()) viewHelper.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getScheduleByServiceApiCall(idService, date!!, adm, idBranch)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .map { obj: ScheduleList -> obj.response }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: List<ScheduleResponse>? ->
                viewHelper.hideLoading()
                viewHelper.updateCalendar(date, response)
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$getScheduleByService "
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper.hideLoading()
                viewHelper.showErrorScreen()
                cd.dispose()
            })
    }

    fun unSubscribe() {
        viewHelper.hideLoading()
    }

    val allHospitalBranch: Unit
        get() {
            viewHelper.showLoading()

            mainScope.launch {
                kotlin.runCatching {
                    networkManager2.getAllHospitalBranch(prefManager.currentUserInfo!!.idCenter.toString())
                }
                    .onSuccess {
                        if (viewHelper == null) {
                            return@onSuccess
                        }
                        Collections.sort(it.response)
                        viewHelper.setHospitalBranch(it.response)
                        viewHelper.hideLoading()
                    }.onFailure {
                        Timber.tag("my").e(LoggingTree.getMessageForError(it, "SchedulePresenter\$getAllHospitalBranch "))
                        if (viewHelper == null) {
                            return@onFailure
                        }
                        viewHelper.hideLoading()
                        viewHelper.showErrorScreen()
                    }
            }
        }

    val currentHospitalBranchId: Int
        get() = prefManager.currentUserInfo!!.idBranch!!

    fun setShowTooltipForSchedule() {
        prefManager.setShowTooltipSchedule()
    }

    fun selectedNewHospitalBranch(newBranch: Int) {
        viewHelper.showLoading()
        val oUser: Int = prefManager.currentUserInfo!!.idUser!!
        val oBranch: Int = prefManager.currentUserInfo!!.idBranch!!
        val cd = CompositeDisposable()
        cd.add(networkManager
            .sendNewFavoriteHospitalBranch(oUser, oBranch, newBranch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response: SimpleResponseString ->
                if (viewHelper == null) {
                    return@subscribe
                }
                idUserFavouriteBranch = response.response
                viewHelper.getDataFrom()
                cd.dispose()
            }) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$selectedNewHospitalBranch "
                    )
                )
                if (viewHelper == null) {
                    return@subscribe
                }
                viewHelper.showError(R.string.api_default_error)
                viewHelper.hideLoading()
                cd.dispose()
            })
    }

    fun getInfoAboutDoc(idDoc: Int) {
        viewHelper.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .getDoctorApiCall(idDoc)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: DoctorList ->
                    viewHelper.showInfoAboutDoctor(response.response[0])
                    viewHelper.hideLoading()
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$getInfoAboutDoc "
                    )
                )
                viewHelper.hideLoading()
                viewHelper.showError("Произошла ошибка при выполнении операции")
                cd.dispose()
            })
    }

    fun rewriteAppointment_cancellation(
        idUserC: Int,
        id_RecordC: Int,
        idBranchC: Int,
        idSotrA: Int,
        dateA: String,
        timeA: String,
        idSpecA: Int,
        idServiceA: Int,
        durationA: Int,
        idUserA: String?,
        idBranchA: Int
    ) {
        val cause = "Перенос приема"
        viewHelper.showLoading()
        CompositeDisposable().add(networkManager
            .sendCancellationOfVisit(idUserC, id_RecordC, cause, idBranchC)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: VisitsOkAndCancelResponse ->
                    Timber.tag("my").v("Отмена приема id записи $id_RecordC причина $cause")
                    if (response.response) {
                        makeAnAppointment(
                            idSotrA,
                            dateA,
                            timeA,
                            idSpecA,
                            idServiceA,
                            durationA,
                            idUserA,
                            idBranchA
                        )
                    }
                }
            ) { throwable: Throwable? ->
                viewHelper.hideLoading()
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$cancellationOfVisit "
                    )
                )
                viewHelper.showError("Произошла ошибка при выполнении операции, попробуйте повторить")
            })
    }

    fun makeAnAppointment(
        idSotr: Int,
        date: String,
        time: String,
        idSpec: Int,
        idService: Int,
        duration: Int,
        idUser: String?,
        idBranch: Int
    ) {
        //getMvpView().showLoading();
        if (!viewHelper.isLoading()) viewHelper.showLoading()
        val cd = CompositeDisposable()
        cd.add(networkManager
            .sendToDoctorVisit(
                idSotr,
                date,
                time,
                idSpec,
                idService,
                duration,
                idBranch,
                idUser!!
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response: ResponseString ->
                    Timber.tag("my")
                        .v("Запись на прием: id доктора $idSotr, дата $date, время $time, id доктора $idSpec, id услуги $idService, продолжительность $duration")
                    if (response.response == "true") {
                        viewHelper.hideLoading()
                        viewHelper.showAlertRecordedSuccessfully(prefManager.centerInfo!!.comment_to_record)
                    } else {
                        viewHelper.hideLoading()
                        viewHelper.showAlertRecordingNotPossible(response.response)
                    }
                    cd.dispose()
                }
            ) { throwable: Throwable? ->
                Timber.tag("my").e(
                    LoggingTree.getMessageForError(
                        throwable,
                        "SchedulePresenter\$makeAnAppointment "
                    )
                )
                viewHelper.hideLoading()
                viewHelper.showError("Произошла ошибка при выполнении операции, старая запись на прием удалена, новая не вставлена. Проверьте свои предстоящие приемы!")
                cd.dispose()
            }
        )
    }

    fun getBranchByIdService(idService: Int) {
        viewHelper.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager2.getBranchByIdService(idService,prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    viewHelper.setHospitalBranch(it.response)
                    if (it.response[0].nameBranch == null) {
                        viewHelper.hideLoading()
                    }
                }.onFailure {
                    Timber.tag("my").e(LoggingTree.getMessageForError(it, "SchedulePresenter\$getBranchByIdService "))
                    viewHelper.hideLoading()
                    viewHelper.showError("Произошла ошибка при выполнении операции")
                }
        }

//        val cd = CompositeDisposable()
//        cd.add(networkManager
//            .getBranchByIdService(idService)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { response: SettingsAllBaranchHospitalList ->
//                    viewHelper.setHospitalBranch(response.response)
//                    if (response.response[0].nameBranch == null) {
//                        viewHelper.hideLoading()
//                    }
//                }
//            ) { throwable: Throwable? ->
//                Timber.tag("my").e(
//                    LoggingTree.getMessageForError(
//                        throwable,
//                        "SchedulePresenter\$getBranchByIdService "
//                    )
//                )
//                viewHelper.hideLoading()
//                viewHelper.showError("Произошла ошибка при выполнении операции")
//                cd.dispose()
//            })
    }

    fun getBranchByIdServiceIdDoc(idService: Int, idDoc: Int) {
        viewHelper.showLoading()

        mainScope.launch {
            kotlin.runCatching {
                networkManager2.getBranchByIdServiceIdDoc(idService, idDoc,prefManager.currentUserInfo!!.apiKey!!, prefManager.centerInfo!!.db_name!!,prefManager.currentUserInfo!!.idUser.toString(),prefManager.currentUserInfo!!.idBranch.toString())
            }
                .onSuccess {
                    viewHelper.setHospitalBranch(it.response)
                    if (it.response[0].nameBranch == null) {
                        viewHelper.hideLoading()
                    }
                }.onFailure {
                    Timber.tag("my").e(LoggingTree.getMessageForError(it, "SchedulePresenter\$getBranchByIdService "))
                    viewHelper.hideLoading()
                    viewHelper.showError("Произошла ошибка при выполнении операции")
                }
        }

//
//        val cd = CompositeDisposable()
//        cd.add(networkManager2
//            .getBranchByIdServiceIdDoc(idService, idDoc,)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { response: SettingsAllBaranchHospitalList ->
//                    viewHelper.setHospitalBranch(
//                        response.response
//                    )
//                }
//            ) { throwable: Throwable? ->
//                Timber.tag("my").e(
//                    LoggingTree.getMessageForError(
//                        throwable,
//                        "SchedulePresenter\$getBranchByIdService "
//                    )
//                )
//                viewHelper.hideLoading()
//                viewHelper.showError("Произошла ошибка при выполнении операции")
//                cd.dispose()
//            })
    }

    fun setNewIdUserFavouriteBranch(idUser: String?) {
        idUserFavouriteBranch = idUser
    }

    fun onDestroy() {
        mainScope.cancel()
    }


}