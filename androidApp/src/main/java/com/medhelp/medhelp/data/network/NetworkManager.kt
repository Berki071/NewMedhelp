package com.medhelp.medhelp.data.network

import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.medhelp.medhelp.data.model.*
import com.medhelp.medhelp.data.model.analise_price.AnalisePriceList
import com.medhelp.medhelp.data.model.chat.MessageFromServerList
import com.medhelp.medhelp.data.model.chat.SimpleResBoolean
import com.medhelp.medhelp.data.model.notification.NotificationMsgList
import com.medhelp.medhelp.data.model.osn.CheckSpamRecordList
import com.medhelp.medhelp.data.model.settings.AllTabsList
import com.medhelp.medhelp.data.model.settings.SimpleResponseBoolean
import com.medhelp.medhelp.data.model.settings.SimpleResponseString
import com.medhelp.medhelp.data.model.yandex_cashbox.PaymentInformationModel
import com.medhelp.medhelp.data.model.yandex_cashbox.PaymentModel
import com.medhelp.medhelp.data.pref.PreferencesManager
import com.medhelp.medhelp.ui.tax_certifacate.DataForTaxCertificate
import com.medhelp.medhelp.utils.main.TimesUtils
import com.medhelp.shared.network.CenterEndPoint
import com.medhelp.shared.network.LocalEndPoint
import com.medhelp.shared.network.NetworkManager.Companion.ADM_DATE
import com.medhelp.shared.network.NetworkManager.Companion.ADM_TIME
import com.medhelp.shared.network.NetworkManager.Companion.AMOUNT
import com.medhelp.shared.network.NetworkManager.Companion.AUTH
import com.medhelp.shared.network.NetworkManager.Companion.DATATODAY
import com.medhelp.shared.network.NetworkManager.Companion.DATE
import com.medhelp.shared.network.NetworkManager.Companion.DB_NAME
import com.medhelp.shared.network.NetworkManager.Companion.DURATION
import com.medhelp.shared.network.NetworkManager.Companion.FROM
import com.medhelp.shared.network.NetworkManager.Companion.ID_BRANCH
import com.medhelp.shared.network.NetworkManager.Companion.ID_BRANCH_NEW
import com.medhelp.shared.network.NetworkManager.Companion.ID_CENTER
import com.medhelp.shared.network.NetworkManager.Companion.ID_DOCTOR
import com.medhelp.shared.network.NetworkManager.Companion.ID_FCM
import com.medhelp.shared.network.NetworkManager.Companion.ID_FILIAL
import com.medhelp.shared.network.NetworkManager.Companion.ID_KL
import com.medhelp.shared.network.NetworkManager.Companion.ID_ROOM
import com.medhelp.shared.network.NetworkManager.Companion.ID_SERVICE
import com.medhelp.shared.network.NetworkManager.Companion.ID_SPEC
import com.medhelp.shared.network.NetworkManager.Companion.ID_USER
import com.medhelp.shared.network.NetworkManager.Companion.ID_USER_NEW
import com.medhelp.shared.network.NetworkManager.Companion.ID_ZAPISI
import com.medhelp.shared.network.NetworkManager.Companion.INN
import com.medhelp.shared.network.NetworkManager.Companion.MESSAGE
import com.medhelp.shared.network.NetworkManager.Companion.QUANTITY
import com.medhelp.shared.network.NetworkManager.Companion.RATING
import com.medhelp.shared.network.NetworkManager.Companion.SPEC
import com.medhelp.shared.network.NetworkManager.Companion.TIME
import com.medhelp.shared.network.NetworkManager.Companion.TO
import com.medhelp.shared.network.NetworkManager.Companion.TYPE
import com.medhelp.shared.network.NetworkManager.Companion.USERNAME
import com.medhelp.shared.network.NetworkManager.Companion.USERNAME2
import com.medhelp.shared.network.NetworkManager.Companion.VERSION_CODE
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.*
import okhttp3.Credentials.basic
import org.json.JSONObject
import java.io.IOException


class NetworkManager(val prefManager: PreferencesManager) {
    //region local
//    fun requestNewPass(username: String): Observable<SimpleResponseString> {
//        return Rx2AndroidNetworking.get(LocalEndPoint.REQUEST_PASS)
//            .addPathParameter(AUTH, LocalEndPoint.API_KEY)
//            .addPathParameter(ID_USER, username)
//            .build()
//            .getObjectObservable(SimpleResponseString::class.java)
//    }



    fun sendRating(dat: String, rat: Float, msg: String): Observable<ErrorResponse> {
        return Rx2AndroidNetworking.post(LocalEndPoint.SEND_RATING)
            .addHeaders(AUTH, LocalEndPoint.API_KEY)
            .addPathParameter(DATE, dat)
            .addPathParameter(RATING, rat.toString())
            .addPathParameter(MESSAGE, msg)
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_CENTER, prefManager.currentUserInfo!!.idCenter.toString())
            .build()
            .getObjectObservable(ErrorResponse::class.java)
    }

//    fun sendLogToServer(
//        type: String,
//        log: String,
//        versionCode: Int
//    ): Observable<SimpleResponseBoolean> {
//        var idUSer = 0
//        var idBranch = 0
//        var idCenter = 0
//        try {
//            idUSer = prefManager.currentUserInfo!!.idUser!!
//            idBranch = prefManager.currentUserInfo!!.idBranch!!
//            idCenter = prefManager.currentUserInfo!!.idCenter!!
//        } catch (e: Exception) {
//        }
//        return Rx2AndroidNetworking.post(LocalEndPoint.SEND_LOG_TO_SERVER)
//            .addHeaders(AUTH, LocalEndPoint.API_KEY)
//            .addPathParameter(ID_USER, idUSer.toString())
//            .addPathParameter(ID_BRANCH, idBranch.toString())
//            .addPathParameter(ID_CENTER, idCenter.toString())
//            .addPathParameter(TYPE, type)
//            .addPathParameter(VERSION_CODE, versionCode.toString())
//            .addBodyParameter("log", log)
//            .build()
//            .getObjectObservable(SimpleResponseBoolean::class.java)
//    }

    fun sendNewFavoriteHospitalBranchToInnerServer(
        newBranch: Int,
        newUserId: String
    ): Observable<SimpleResponseBoolean> {
        return Rx2AndroidNetworking.post(LocalEndPoint.NEW_FAVORITE_BRANCH)
            .addHeaders(AUTH, LocalEndPoint.API_KEY)
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER_NEW, newUserId)
            .addPathParameter(ID_BRANCH_NEW, newBranch.toString())
            .addPathParameter(ID_CENTER, prefManager.currentUserInfo!!.idCenter.toString())
            .build()
            .getObjectObservable(SimpleResponseBoolean::class.java)
    }

//    fun getNewsUpdate(): Observable<NewsList> {
//        return Rx2AndroidNetworking.get(LocalEndPoint.NEWS_UPDATE)
//            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
//            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
//            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
//            .build()
//            .getObjectObservable(NewsList::class.java)
//    }
    //endregion


    fun getCurrentDateApiCall(): Observable<DateList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.DATE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(DateList::class.java)
    }

    fun getPriceApiCall(
        idDoctor: Int,
        idBranch: Int,
        idUser: Int
    ): Observable<ServiceList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.PRICE_BY_DOCTOR)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_DOCTOR, idDoctor.toString())
            .addPathParameter(ID_BRANCH, idBranch.toString())
            .addPathParameter(ID_USER, idUser.toString())
            .build()
            .getObjectObservable(ServiceList::class.java)
    }

    fun getTabPrice(): Observable<AllTabsList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.PRICE_FAVORITE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .build()
            .getObjectObservable(AllTabsList::class.java)
    }

    fun getPriceApiCall(): Observable<ServiceList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.PRICE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(ServiceList::class.java)
    }

    fun getCategoryApiCall(): Observable<SpecialtyList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.CATEGORY)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(SpecialtyList::class.java)
    }

    fun getCategoryApiCall(idDoctor: Int): Observable<SpecialtyList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.CATEGORY_BY_ID_DOCTOR)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_DOCTOR, idDoctor.toString())
            .build()
            .getObjectObservable(SpecialtyList::class.java)
    }

    fun getDoctorApiCall(idDoctor: Int): Observable<DoctorList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.DOCTOR_BY_ID)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_DOCTOR, idDoctor.toString())
            .build()
            .getObjectObservable(DoctorList::class.java)
    }

    fun getSaleApiCall(date: String): Observable<SaleList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.SALE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ADM_DATE, date)
            .build()
            .getObjectObservable(SaleList::class.java)
    }

    fun getAllReceptionApiCall(): Observable<VisitList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.VISITS)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(VisitList::class.java)
    }

    fun getScheduleByDoctorApiCall(
        idDoctor: Int,
        date: String,
        adm: Int,
        branch: Int
    ): Observable<ScheduleList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.SCHEDULE_DOCTOR)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_DOCTOR, idDoctor.toString())
            .addPathParameter(ADM_DATE, date)
            .addPathParameter(ADM_TIME, adm.toString())
            .addPathParameter(ID_BRANCH, branch.toString())
            .build()
            .getObjectObservable(ScheduleList::class.java)
    }

    fun getScheduleByServiceApiCall(
        idService: Int,
        date: String,
        adm: Int,
        branch: Int
    ): Observable<ScheduleList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.SCHEDULE_SERVICE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_SERVICE, idService.toString())
            .addPathParameter(ADM_DATE, date)
            .addPathParameter(ADM_TIME, adm.toString())
            .addPathParameter(ID_BRANCH, branch.toString())
            .build()
            .getObjectObservable(ScheduleList::class.java)
    }

    fun sendCancellationOfVisit(
        user: Int,
        id_zapisi: Int,
        cause: String,
        idBranch: Int
    ): Observable<VisitsOkAndCancelResponse> {
        return Rx2AndroidNetworking.get(CenterEndPoint.VISITS_CANCEL)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, user.toString())
            .addPathParameter(ID_ZAPISI, id_zapisi.toString())
            .addPathParameter(MESSAGE, cause)
            .addPathParameter(DATATODAY, TimesUtils.getCurrentDate(TimesUtils.DATE_FORMAT_ddMMyy))
            .addPathParameter(ID_BRANCH, idBranch.toString())
            .build()
            .getObjectObservable(VisitsOkAndCancelResponse::class.java)
    }

    fun sendConfirmationOfVisit(
        user: Int,
        id_zapisi: Int,
        idBranch: Int
    ): Observable<VisitsOkAndCancelResponse> {
        return Rx2AndroidNetworking.get(CenterEndPoint.VISITS_OK)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, user.toString())
            .addPathParameter(ID_ZAPISI, id_zapisi.toString())
            .addPathParameter(ID_BRANCH, idBranch.toString())
            .build()
            .getObjectObservable(VisitsOkAndCancelResponse::class.java)
    }

    fun sendToDoctorVisit(
        id_doctor: Int,
        date: String,
        id_time: String,
        id_spec: Int,
        id_ysl: Int,
        duration: Int,
        idBranch: Int,
        idUser: String
    ): Observable<ResponseString> {
        return Rx2AndroidNetworking.get(CenterEndPoint.RECORD_TO_THE_DOCTOR)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_DOCTOR, id_doctor.toString())
            .addPathParameter(DATE, date)
            .addPathParameter(TIME, id_time)
            .addPathParameter(ID_USER, idUser)
            .addPathParameter(ID_SPEC, id_spec.toString())
            .addPathParameter(ID_SERVICE, id_ysl.toString())
            .addPathParameter(DURATION, duration.toString())
            .addPathParameter(ID_BRANCH, idBranch.toString())
            .build()
            .getObjectObservable(ResponseString::class.java)
    }

    fun getResultAnalysis(): Observable<AnaliseResultResponse> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_RESULT_ANALIZ)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(AnaliseResultResponse::class.java)
    }

    fun getResultAnalyzesList(): Observable<AnalysisListResponse> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_RESULT_ANALIZ_LIST)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(AnalysisListResponse::class.java)
    }

    fun getAllDoctors(): Observable<AllDoctorsList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_ALL_DOCTORS)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(AllDoctorsList::class.java)
    }

    fun sendNewFavoriteHospitalBranch(
        oldIdUser: Int,
        oldBranch: Int,
        newBranch: Int
    ): Observable<SimpleResponseString> {
        return Rx2AndroidNetworking.post(CenterEndPoint.NEW_FAVORITE_BRANCH)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, oldIdUser.toString())
            .addPathParameter(ID_BRANCH, oldBranch.toString())
            .addPathParameter(ID_BRANCH_NEW, newBranch.toString())
            .build()
            .getObjectObservable(SimpleResponseString::class.java)
    }

//    fun getAllRoom(idUser: Int, idBranch: Int): Observable<InfoAboutDocList> {
//        return Rx2AndroidNetworking.get(CenterEndPoint.ALL_ROOM)
//            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
//            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
//            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
//            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
//            .addPathParameter(ID_USER, idUser.toString())
//            .addPathParameter(ID_BRANCH, idBranch.toString())
//            .build()
//            .getObjectObservable(InfoAboutDocList::class.java)
//    }

    fun getAllExternalMsg(idUser: Int, idBranch: Int): Observable<MessageFromServerList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.EXTERNAL_MSG)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, idUser.toString())
            .addPathParameter(ID_BRANCH, idBranch.toString())
            .build()
            .getObjectObservable(MessageFromServerList::class.java)
    }

    //чат, тестить при запуске чата
    fun sendOurMsgToServer(
        idRoom: Long,
        msg: String,
        type: String
    ): Observable<SimpleResBoolean> {
        return Rx2AndroidNetworking.post(CenterEndPoint.SEND_MSG)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_ROOM, idRoom.toString())
            .addBodyParameter(MESSAGE, msg)
            .addPathParameter(TYPE, type)
            .setPriority(Priority.MEDIUM)
            .build()
            .getObjectObservable(SimpleResBoolean::class.java)
    }

    fun getAllNotification(idUser: Int, idBranch: Int): Observable<NotificationMsgList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_ALL_NOTIFICATION)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, idUser.toString())
            .addPathParameter(ID_BRANCH, idBranch.toString())
            .build()
            .getObjectObservable(NotificationMsgList::class.java)
    }

    fun insertFavoritesService(service: Int): Observable<SimpleResponseBoolean> {
        return Rx2AndroidNetworking.post(CenterEndPoint.IZBRAN_PRAIS_INSERT)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_SERVICE, service.toString())
            .build()
            .getObjectObservable(SimpleResponseBoolean::class.java)
    }

    fun deleteFavoritesService(service: Int): Observable<SimpleResponseBoolean> {
        return Rx2AndroidNetworking.post(CenterEndPoint.IZBRAN_PRAIS_DELETE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_SERVICE, service.toString())
            .build()
            .getObjectObservable(SimpleResponseBoolean::class.java)
    }

    fun checkSpamRecord(service: Int): Observable<CheckSpamRecordList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.CHECK_SPAM)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_SERVICE, service.toString())
            .build()
            .getObjectObservable(CheckSpamRecordList::class.java)
    }

    fun getAnalisePrice(): Observable<AnalisePriceList> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_ALL_ALALISE_PRICE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(AnalisePriceList::class.java)
    }

    fun sendImageToService(ip: String, json: JSONObject): Observable<Boolean> {
        return Rx2AndroidNetworking.post(ip)
            .addHeaders("Content-Type", "application/json")
            .setContentType("application/json; charset=utf-8")
            .addJSONObjectBody(json)
            .setPriority(Priority.HIGH)
            .build()
            .getObjectObservable(Boolean::class.java)
    }


//***
//    fun getBranchByIdService(idService: Int): Observable<SettingsAllBaranchHospitalList> {
//        return Rx2AndroidNetworking.get(CenterEndPoint.GET_BRANCH_BY_ID_SERVICE)
//            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
//            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
//            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
//            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
//            .addPathParameter(ID_SERVICE, idService.toString())
//            .build()
//            .getObjectObservable(SettingsAllBaranchHospitalList::class.java)
//    }

    //***
//    fun getBranchByIdServiceIdDoc(idService: Int, idDoc: Int): Observable<SettingsAllBaranchHospitalList> {
//        return Rx2AndroidNetworking.get(CenterEndPoint.GET_BRANCH_BY_ID_SERVICE_ID_SOTR)
//            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
//            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
//            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
//            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
//            .addPathParameter(ID_SERVICE, idService.toString())
//            .addPathParameter(ID_DOCTOR, idDoc.toString())
//            .build()
//            .getObjectObservable(SettingsAllBaranchHospitalList::class.java)
//    }

    fun sendToServerPaymentData(
        idUser: String,
        idBranch: String,
        idZapisi: String,
        idService: String,
        amount: String,
        count: Int,
        paymentId: String
    ): Observable<SimpleResBoolean> {
        return Rx2AndroidNetworking.post(CenterEndPoint.SEND_TO_SERVER_PAYMENT_DATA)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, idUser)
            .addPathParameter(ID_BRANCH, idBranch)
            .addPathParameter(ID_ZAPISI, idZapisi)
            .addPathParameter(ID_SERVICE, idService)
            .addPathParameter(AMOUNT, amount)
            .addPathParameter(QUANTITY, count.toString())
            .addBodyParameter("pay_id", paymentId)
            .build()
            .getObjectObservable(SimpleResBoolean::class.java)
    }

    fun sendIAmHere(
        idUser: Int,
        idZapisi: Int,
        idBranch: Int
    ): Observable<SimpleResBoolean> {
        return Rx2AndroidNetworking.post(CenterEndPoint.I_AM_HERE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, idUser.toString())
            .addPathParameter(ID_ZAPISI, idZapisi.toString())
            .addPathParameter(ID_BRANCH, idBranch.toString())
            .build()
            .getObjectObservable(SimpleResBoolean::class.java)
    }

    //region yandexCashbox
    private fun authenticate(idShop: String, keyShope: String): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(object : Authenticator {
                @Throws(IOException::class)
                override fun authenticate(route: Route?, response: Response): Request {
                    return response.request.newBuilder()
                        .header("Authorization", basic(idShop, keyShope))
                        .build()
                }
            })
            .build()
    }

    fun sendPayment(
        `object`: JSONObject,
        keyIdempotence: String,
        idShop: String,
        keyShope: String
    ): Observable<PaymentModel> {
        return Rx2AndroidNetworking.post("https://api.yookassa.ru/v3/payments")
            .setOkHttpClient(authenticate(idShop, keyShope))
            .addHeaders("Idempotence-Key", keyIdempotence)
            .addHeaders("Content-Type", "application/json")
            .addJSONObjectBody(`object`)
            .setPriority(Priority.HIGH)
            .build()
            .getObjectObservable(PaymentModel::class.java)
    }

    fun getPaymentInformation(
        paymentId: String,
        idShop: String,
        keyShope: String
    ): Observable<PaymentInformationModel> {
        return Rx2AndroidNetworking.get("https://api.yookassa.ru/v3/payments/$paymentId")
            .setOkHttpClient(authenticate(idShop, keyShope))
            .setPriority(Priority.HIGH)
            .build()
            .getObjectObservable(PaymentInformationModel::class.java)
    }

    //endregion
//    fun sendFcmId(idUser: String, idFilial: String, idFcm: String): Observable<SimpleResBoolean> {
//        return Rx2AndroidNetworking.get(CenterEndPoint.SEND_FCM_ID)
//            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
//            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
//            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
//            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
//            .addPathParameter(ID_USER, idUser)
//            .addPathParameter(ID_BRANCH, idFilial)
//            .addPathParameter(ID_FCM, idFcm)
//            .build()
//            .getObjectObservable(SimpleResBoolean::class.java)
//    }

    fun getTechUsersFcm(): Observable<TechUsersFcmIdResponse> {
        return Rx2AndroidNetworking.get(LocalEndPoint.GET_USERS_TECH_FCM_ID)
            .build()
            .getObjectObservable(TechUsersFcmIdResponse::class.java)
    }

    fun sendMsgToSupport(
        login: String,
        email: String,
        msg: String
    ): Observable<SimpleResBoolean> {
        return Rx2AndroidNetworking.post(LocalEndPoint.SEND_MSG_TO_SUPPORT)
            .addBodyParameter("username", login)
            .addBodyParameter("email", email)
            .addBodyParameter("message", msg)
            .build()
            .getObjectObservable(SimpleResBoolean::class.java)
    }

    fun sendMsgFCM(
        json: JSONObject,
        serverKey: String,
        senderId: String
    ): Observable<FCMResponse> {
        return Rx2AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
            .addHeaders("Authorization", "key=$serverKey")
            .addHeaders("Sender", "id=$senderId")
            .setContentType("application/json")
            .addJSONObjectBody(json)
            .setPriority(Priority.HIGH)
            .build()
            .getObjectObservable(FCMResponse::class.java)
    }

    fun getAllBonuses(): Observable<BonusesResponse> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_ALL_BONUSES)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_CENTER, prefManager.currentUserInfo!!.idCenter.toString())
            .build()
            .getObjectObservable(BonusesResponse::class.java)
    }

    fun getResultZakl(): Observable<ResultZaklResponse> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_RESULT_ZAKL)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(ResultZaklResponse::class.java)
    }

    fun getResultZakl2(): Observable<ResultZakl2Response> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_ZAKL_AMB2)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, prefManager.currentUserInfo!!.idUser.toString())
            .addPathParameter(ID_BRANCH, prefManager.currentUserInfo!!.idBranch.toString())
            .build()
            .getObjectObservable(ResultZakl2Response::class.java)
    }

    fun geDataResultZakl2(item: ResultZakl2Item): Observable<LoadDataZaklAmbResponse> {
        return Rx2AndroidNetworking.get(CenterEndPoint.GET_DATA_FOR_ZAKL2)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, item.idKl)
            .addPathParameter(ID_BRANCH, item.idFilial)
            .addPathParameter(SPEC, item.nameSpec)
            .addPathParameter(DATE, item.dataPriema)
            .build()
            .getObjectObservable(LoadDataZaklAmbResponse::class.java)
    }

    fun sendDataForTaxCertificate(data: DataForTaxCertificate): Observable<SendTaxCertificateResponseList> {
//        var DB_NAME1 = prefManager.centerInfo!!.db_name
//        var AUTH1 = prefManager.currentUserInfo!!.apiKey
//        var ID_KL1 = prefManager.currentUserInfo!!.idUser.toString()
//        var ID_FILIAL1 = prefManager.currentUserInfo!!.idBranch.toString()

        return Rx2AndroidNetworking.get(CenterEndPoint.SEND_DATA_FOR_TAX_CERTIFICATE)
            .addHeaders(DB_NAME, prefManager.centerInfo!!.db_name)
            .addHeaders(AUTH, prefManager.currentUserInfo!!.apiKey)
            .addHeaders(ID_KL, prefManager.currentUserInfo!!.idUser.toString())
            .addHeaders(ID_FILIAL, prefManager.currentUserInfo!!.idBranch.toString())
            .addPathParameter(ID_USER, data.idUser)
            .addPathParameter(ID_BRANCH, data.idBranch)
            .addPathParameter(FROM, data.dateFrom)
            .addPathParameter(TO, data.dateTo)
            .addPathParameter(INN, data.inn)
            .addPathParameter(USERNAME, data.fioPacienta) //..fio_pac/:fio_nalog;
            .addPathParameter(USERNAME2, data.fioNalogoplat)
            .build()
            .getObjectObservable(SendTaxCertificateResponseList::class.java)
    }

    fun loadFileZakl(
        data: LoadDataZaklAmbItem,
        dirPath: String,
        fileName: String,
        listener: Load2FileListener,
        item: ResultZakl2Item
    ): Completable {
        return Completable.fromAction {
            Rx2AndroidNetworking.post("https://oneclick.tmweb.ru/medhelp_client/fpdf/report_ambkarti.php")
                .setContentType("application/pdf")
                .addBodyParameter("data_priem", data.dataPriem)
                .addBodyParameter("diagnoz", data.diagnoz)
                .addBodyParameter("rekomend", data.rekomend)
                .addBodyParameter("sotr", data.sotr)
                .addBodyParameter("sotr_spec", data.sotrSpec)
                .addBodyParameter("cons", data.cons)
                .addBodyParameter("shapka", data.shapka)
                .addBodyParameter("nom_amb", data.nom_amb)
                .addBodyParameter("OOO", data.ooo)
                .addBodyParameter(
                    "fiokl",
                    prefManager.currentUserInfo!!.surname + " " + prefManager.currentUserInfo!!.name + " " + prefManager.currentUserInfo!!.patronymic
                )
                .build()
                .getAsOkHttpResponse(object : OkHttpResponseListener {
                    override fun onResponse(response: Response) {
                        listener.onResponse(response, dirPath, fileName, item)
                    }

                    override fun onError(anError: ANError) {
                        listener.onError(anError, item)
                    }
                })
        }
    }

    fun loadFileTax(
        data: SendTaxCertificateResponse,
        dirPath: String,
        fileName: String,
        listener: Load2FileListenerTax,
        item: DataForTaxCertificate
    ): Completable {
        return Completable.fromAction {
            Rx2AndroidNetworking.post("https://oneclick.tmweb.ru/medhelp_client/fpdf/report_spravka_v_nalog.php")
                .setContentType("application/pdf")
                .addBodyParameter("nom_doc", data.nom_doc)
                .addBodyParameter("fio_nalogoplat", data.fio_nalogoplat)
                .addBodyParameter("inn", data.inn)
                .addBodyParameter("fio_pac", data.fio_pac)
                .addBodyParameter("nom_amb", data.nom_amb)
                .addBodyParameter("itogo", data.itogo)
                .addBodyParameter("itogo_propis", data.itogo_propis)
                .addBodyParameter("dati_oplat", data.dati_oplat)
                .addBodyParameter("OOO", data.ooo)
                .addBodyParameter("rekviziti", data.rekviziti)
                .addBodyParameter("licenziya", data.licenziya)
                .addBodyParameter("min_data", data.min_data)
                .addBodyParameter("kto_vidal", data.kto_vidal)
                .addBodyParameter("telefon", data.telefon)
                .addBodyParameter("dbname", data.dbname)
                .build()
                .getAsOkHttpResponse(object : OkHttpResponseListener {
                    override fun onResponse(response: Response) {
                        listener.onResponse(response, dirPath, fileName, item)
                    }

                    override fun onError(anError: ANError) {
                        listener.onError(anError, item)
                    }
                })
        }
    }

    interface Load2FileListener {
        fun onResponse(response: Response, dirPath: String, fileName: String, item: ResultZakl2Item)
        fun onError(anError: ANError, item: ResultZakl2Item)
    }

    interface Load2FileListenerTax {
        fun onResponse(
            response: Response,
            dirPath: String,
            fileName: String,
            item: DataForTaxCertificate
        )

        fun onError(anError: ANError, item: DataForTaxCertificate)
    }
}