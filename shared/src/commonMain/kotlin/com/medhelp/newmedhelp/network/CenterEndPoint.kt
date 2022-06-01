package com.medhelp.shared.network

interface CenterEndPoint {
    companion object {
        const val BASE_URL = "https://oneclick.tmweb.ru/medhelp_client/v1/"
        const val DOCTOR_BY_ID = BASE_URL + "sotr_info/{id_doctor}" //+
        const val CATEGORY = BASE_URL + "specialty" //+
        const val CATEGORY_BY_ID_DOCTOR = BASE_URL + "specialty/doctor/{id_doctor}" //+
        const val PRICE = BASE_URL + "services/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val PRICE_FAVORITE = BASE_URL + "IzbranPraisOnly/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val PRICE_BY_DOCTOR = BASE_URL + "services/doctor/{id_doctor}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_USER + "}" //+
        const val VISITS = BASE_URL + "visits/{id_user}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val SALE = BASE_URL + "sale/{date}" //+
        const val SCHEDULE_DOCTOR = BASE_URL + "schedule/doctor/{id_doctor}/{date}/{adm}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val SCHEDULE_SERVICE = BASE_URL + "schedule/service/{id_service}/{date}/{adm}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val VISITS_CANCEL = BASE_URL + "visits_cancel/{id_user}/{id_zapisi}/{message}/{datatoday}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val VISITS_OK = BASE_URL + "visits_ok/{id_user}/{id_zapisi}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val RECORD_TO_THE_DOCTOR = BASE_URL + "record/{" + NetworkManager.ID_DOCTOR + "}/{" + NetworkManager.DATE + "}/{" + NetworkManager.TIME + "}/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_SPEC + "}" +
                    "/{" + NetworkManager.ID_SERVICE + "}/{" + NetworkManager.DURATION + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val GET_RESULT_ANALIZ = BASE_URL + "getResultAnaliz/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val GET_RESULT_ANALIZ_LIST = BASE_URL + "getAllAnaliz/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val GET_ALL_DOCTORS = BASE_URL + "doctors" //+
        const val NEW_FAVORITE_BRANCH = BASE_URL + "SmenaLikeFilial/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_BRANCH_NEW + "}" //+
        const val ALL_ROOM = BASE_URL + "ChatAllRoomsKL/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val EXTERNAL_MSG = BASE_URL + "ChatNewTextKL/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val SEND_MSG = BASE_URL + "ChatInsertNewText/{" + NetworkManager.ID_ROOM + "}/vvv/client/{" + NetworkManager.TYPE + "}" //+
        const val GET_ALL_NOTIFICATION = BASE_URL + "GetNotifications/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
        const val DATE = BASE_URL + "date" //+
        const val IZBRAN_PRAIS_INSERT = BASE_URL + "IzbranPraisInsert/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_SERVICE + "}" //+
        const val IZBRAN_PRAIS_DELETE = BASE_URL + "IzbranPraisDelete/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_SERVICE + "}" //+
        const val CHECK_SPAM = BASE_URL + "CheckSpamZapis/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_SERVICE + "}" //+
        const val GET_ALL_ALALISE_PRICE = BASE_URL + "getAnalizPrice" //+
//        const val GET_USER_INFO = BASE_URL + "ClientInfoById/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}" //+
//        const val GET_BRANCH_BY_ID_SERVICE = BASE_URL + "FilialByIdYsl/{" + NetworkManager.ID_SERVICE + "}" //+
//        const val GET_BRANCH_BY_ID_SERVICE_ID_SOTR = BASE_URL + "FilialByIdYslIdSotr/{" + NetworkManager.ID_SERVICE + "}/{" + NetworkManager.ID_DOCTOR + "}" //+
        const val SEND_TO_SERVER_PAYMENT_DATA = BASE_URL + "OnlinePayment/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_ZAPISI +
                    "}/{" + NetworkManager.ID_SERVICE + "}/{" + NetworkManager.AMOUNT + "}/{" + NetworkManager.QUANTITY + "}" //+
        const val I_AM_HERE = BASE_URL + "iamhere/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_ZAPISI + "}/{" + NetworkManager.ID_BRANCH + "}" //+
       // const val SEND_FCM_ID = BASE_URL + "UpdateFCMuser/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.ID_FCM + "}"
        const val GET_ALL_BONUSES = BASE_URL + "BonusCardHistory/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_CENTER + "}"
        const val SEND_DATA_FOR_TAX_CERTIFICATE = BASE_URL + "SpravkaNalog/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.FROM + "}/{" + NetworkManager.TO + "}/{" + NetworkManager.INN +
                    "}/{" + NetworkManager.USERNAME + "}/{" + NetworkManager.USERNAME2 + "}" //..fio_pac/:fio_nalog;
        const val GET_RESULT_ZAKL = BASE_URL + "getResultZakl/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}"
        const val GET_ZAKL_AMB2 = BASE_URL + "GetZaklAmb/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}"
        const val GET_DATA_FOR_ZAKL2 = BASE_URL + "LoadZaklAmb/{" + NetworkManager.ID_USER + "}/{" + NetworkManager.ID_BRANCH + "}/{" + NetworkManager.SPEC + "}/{" + NetworkManager.DATE + "}"
    }
}